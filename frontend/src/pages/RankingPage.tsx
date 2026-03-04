import { useState, useMemo, useRef, useEffect } from 'react'
import { useTopTen, usePopularityByRegion, usePopularityByGenreRegion } from '@/hooks/useRanking'
import { usePerformances } from '@/hooks/usePerformances'
import PerformanceGrid from '@/components/performance/PerformanceGrid'
import ErrorFallback from '@/components/common/ErrorFallback'
import { TrendingUp, Loader2, Trophy, MapPin, Music, Inbox, Search, ChevronDown, X } from 'lucide-react'
import type { RankingCount, Performance } from '@/types/performance'

type Tab = 'topten' | 'facility' | 'genre'

const tabs: { key: Tab; label: string; icon: typeof Trophy }[] = [
  { key: 'topten', label: 'TOP 10', icon: Trophy },
  { key: 'facility', label: '시설별', icon: MapPin },
  { key: 'genre', label: '장르별', icon: Music },
]

export default function RankingPage() {
  const [activeTab, setActiveTab] = useState<Tab>('topten')
  const { data: performances } = usePerformances()

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-2">
        <TrendingUp className="h-5 w-5 text-coral" />
        <h1 className="text-xl font-bold">인기 공연</h1>
      </div>

      <div className="flex gap-1 rounded-2xl border border-border/60 bg-card p-1 shadow-sm">
        {tabs.map(({ key, label, icon: Icon }) => (
          <button
            key={key}
            onClick={() => setActiveTab(key)}
            className={`flex flex-1 items-center justify-center gap-1.5 rounded-xl px-3 py-2.5 text-sm font-medium transition-all ${
              activeTab === key
                ? 'bg-primary text-primary-foreground shadow-md'
                : 'text-muted-foreground hover:text-foreground hover:bg-muted/50'
            }`}
          >
            <Icon className="h-4 w-4" />
            {label}
          </button>
        ))}
      </div>

      {activeTab === 'topten' && <TopTenSection performances={performances ?? []} />}
      {activeTab === 'facility' && <FacilitySection performances={performances ?? []} />}
      {activeTab === 'genre' && <GenreSection performances={performances ?? []} />}
    </div>
  )
}

function EmptyState({ message }: { message: string }) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 py-20">
      <Inbox className="h-12 w-12 text-muted-foreground/50" />
      <p className="text-muted-foreground">{message}</p>
    </div>
  )
}

function CategoryChips({
  items,
  selected,
  onSelect,
}: {
  items: RankingCount[]
  selected: string | null
  onSelect: (name: string) => void
}) {
  return (
    <div className="flex flex-wrap gap-2">
      {items.map((item) => (
        <button
          key={item.name}
          onClick={() => onSelect(item.name)}
          className={`inline-flex items-center gap-1.5 rounded-full border px-3.5 py-1.5 text-sm transition-all ${
            selected === item.name
              ? 'border-primary bg-primary text-primary-foreground shadow-md'
              : 'border-border/60 bg-card text-foreground hover:bg-muted hover:shadow-sm'
          }`}
        >
          <span className="font-medium">{item.name}</span>
          <span className={`text-xs ${selected === item.name ? 'text-primary-foreground/70' : 'text-muted-foreground'}`}>
            {item.count}
          </span>
        </button>
      ))}
    </div>
  )
}

function TopTenSection({ performances }: { performances: Performance[] }) {
  const { data: ranking, isLoading, isError, refetch } = useTopTen()

  const topPerformances = useMemo(() => {
    if (!ranking || !performances.length) return []
    const perfMap = new Map(performances.map((p) => [p.mt20id, p]))
    return ranking
      .map((r) => perfMap.get(r.id ?? ''))
      .filter((p): p is Performance => p !== undefined)
  }, [ranking, performances])

  if (isLoading) return <LoadingState />
  if (isError) return <ErrorFallback message="TOP 10 데이터를 불러올 수 없습니다." onRetry={() => refetch()} />
  if (!ranking || ranking.length === 0) return <EmptyState message="아직 북마크된 공연이 없습니다." />
  if (topPerformances.length === 0) return <EmptyState message="공연 데이터를 불러오는 중입니다." />

  return (
    <div className="space-y-4">
      <p className="text-sm text-muted-foreground">북마크가 많은 인기 공연</p>
      <PerformanceGrid performances={topPerformances} />
    </div>
  )
}

function FacilitySearchSelect({
  items,
  selected,
  onSelect,
}: {
  items: RankingCount[]
  selected: string | null
  onSelect: (name: string | null) => void
}) {
  const [open, setOpen] = useState(false)
  const [query, setQuery] = useState('')
  const ref = useRef<HTMLDivElement>(null)

  useEffect(() => {
    function handleClickOutside(e: MouseEvent) {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false)
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  const filtered = items.filter((item) => item.name.toLowerCase().includes(query.toLowerCase()))

  return (
    <div ref={ref} className="relative">
      <button
        onClick={() => setOpen(!open)}
        className="flex w-full items-center justify-between rounded-xl border border-border/60 bg-card px-4 py-3 text-sm shadow-sm transition-all hover:shadow-md"
      >
        <span className={selected ? 'font-medium text-foreground' : 'text-muted-foreground'}>
          {selected ?? '시설을 선택하세요'}
        </span>
        <div className="flex items-center gap-1">
          {selected && (
            <span
              role="button"
              onClick={(e) => { e.stopPropagation(); onSelect(null); setQuery('') }}
              className="rounded-full p-0.5 hover:bg-muted"
            >
              <X className="h-3.5 w-3.5 text-muted-foreground" />
            </span>
          )}
          <ChevronDown className={`h-4 w-4 text-muted-foreground transition-transform ${open ? 'rotate-180' : ''}`} />
        </div>
      </button>

      {open && (
        <div className="absolute z-10 mt-1 w-full rounded-xl border border-border/60 bg-card shadow-lg">
          <div className="flex items-center gap-2 border-b border-border/40 px-3 py-2">
            <Search className="h-4 w-4 text-muted-foreground" />
            <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="시설명 검색..."
              className="w-full bg-transparent text-sm outline-none placeholder:text-muted-foreground/60"
              autoFocus
            />
          </div>
          <ul className="max-h-60 overflow-y-auto py-1">
            {filtered.length > 0 ? (
              filtered.map((item) => (
                <li key={item.name}>
                  <button
                    onClick={() => { onSelect(item.name); setOpen(false); setQuery('') }}
                    className={`flex w-full items-center justify-between px-4 py-2.5 text-sm transition-colors ${
                      selected === item.name
                        ? 'bg-primary/10 font-medium text-primary'
                        : 'text-foreground hover:bg-muted/50'
                    }`}
                  >
                    <span>{item.name}</span>
                    <span className="text-xs text-muted-foreground">{item.count}개</span>
                  </button>
                </li>
              ))
            ) : (
              <li className="px-4 py-3 text-center text-sm text-muted-foreground">검색 결과가 없습니다</li>
            )}
          </ul>
        </div>
      )}
    </div>
  )
}

function FacilitySection({ performances }: { performances: Performance[] }) {
  const { data: facilities, isLoading, isError, refetch } = usePopularityByRegion()
  const [selected, setSelected] = useState<string | null>(null)

  const filtered = useMemo(() => {
    if (!selected) return []
    return performances.filter((p) => p.fcltynm === selected)
  }, [performances, selected])

  if (isLoading) return <LoadingState />
  if (isError) return <ErrorFallback message="시설별 데이터를 불러올 수 없습니다." onRetry={() => refetch()} />
  if (!facilities || facilities.length === 0) return <EmptyState message="시설별 데이터가 없습니다." />

  return (
    <div className="space-y-6">
      <FacilitySearchSelect items={facilities} selected={selected} onSelect={setSelected} />
      {selected ? (
        filtered.length > 0 ? (
          <PerformanceGrid performances={filtered} />
        ) : (
          <EmptyState message="해당 시설의 공연이 없습니다." />
        )
      ) : (
        <p className="py-10 text-center text-sm text-muted-foreground">시설을 선택하면 해당 공연을 볼 수 있습니다.</p>
      )}
    </div>
  )
}

function GenreSection({ performances }: { performances: Performance[] }) {
  const { data: genres, isLoading, isError, refetch } = usePopularityByGenreRegion()
  const [selected, setSelected] = useState<string | null>(null)

  const filtered = useMemo(() => {
    if (!selected) return []
    return performances.filter((p) => p.genrenm === selected)
  }, [performances, selected])

  if (isLoading) return <LoadingState />
  if (isError) return <ErrorFallback message="장르별 데이터를 불러올 수 없습니다." onRetry={() => refetch()} />
  if (!genres || genres.length === 0) return <EmptyState message="장르별 데이터가 없습니다." />

  return (
    <div className="space-y-6">
      <CategoryChips items={genres} selected={selected} onSelect={setSelected} />
      {selected ? (
        filtered.length > 0 ? (
          <PerformanceGrid performances={filtered} />
        ) : (
          <EmptyState message="해당 장르의 공연이 없습니다." />
        )
      ) : (
        <p className="py-10 text-center text-sm text-muted-foreground">장르를 선택하면 해당 공연을 볼 수 있습니다.</p>
      )}
    </div>
  )
}

function LoadingState() {
  return (
    <div className="flex items-center justify-center py-20">
      <Loader2 className="h-8 w-8 animate-spin text-primary" />
    </div>
  )
}
