import { useState } from 'react'
import { useTopTen, usePopularityByRegion, usePopularityByGenreRegion } from '@/hooks/useRanking'
import { TrendingUp, Loader2, Trophy, MapPin, Music } from 'lucide-react'

type Tab = 'topten' | 'region' | 'genre'

const tabs: { key: Tab; label: string; icon: typeof Trophy }[] = [
  { key: 'topten', label: 'TOP 10', icon: Trophy },
  { key: 'region', label: '지역별', icon: MapPin },
  { key: 'genre', label: '장르별', icon: Music },
]

export default function RankingPage() {
  const [activeTab, setActiveTab] = useState<Tab>('topten')

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

      {activeTab === 'topten' && <TopTenSection />}
      {activeTab === 'region' && <RegionSection />}
      {activeTab === 'genre' && <GenreSection />}
    </div>
  )
}

function TopTenSection() {
  const { data, isLoading } = useTopTen()

  if (isLoading) return <LoadingState />
  if (!data || !Array.isArray(data)) return null

  return (
    <div className="space-y-2.5">
      {data.map((item: Record<string, unknown>, index: number) => {
        const name = (item.name || item.prfnm || '') as string
        const count = (item.count || item.cnt || 0) as number
        const rank = index + 1
        const maxCount = (data[0] as Record<string, unknown>)?.count as number || (data[0] as Record<string, unknown>)?.cnt as number || 1

        return (
          <div
            key={index}
            className="flex items-center gap-4 rounded-2xl border border-border/60 bg-card p-4 shadow-sm transition-all hover:shadow-md"
          >
            <span
              className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-full text-sm font-bold shadow-sm ${
                rank <= 3
                  ? 'bg-gradient-to-br from-primary to-coral text-white'
                  : 'bg-muted text-muted-foreground'
              }`}
            >
              {rank}
            </span>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-semibold text-card-foreground truncate">{name}</p>
              <div className="mt-2 h-2 w-full overflow-hidden rounded-full bg-muted">
                <div
                  className="h-full rounded-full bg-gradient-to-r from-primary to-primary/60 transition-all duration-500"
                  style={{ width: `${(count / maxCount) * 100}%` }}
                />
              </div>
            </div>
            <span className="shrink-0 text-xs font-medium text-muted-foreground">{count.toLocaleString()}</span>
          </div>
        )
      })}
    </div>
  )
}

function RegionSection() {
  const { data, isLoading } = usePopularityByRegion()

  if (isLoading) return <LoadingState />
  if (!data || !Array.isArray(data)) return null

  const maxCount = Math.max(...data.map((item: Record<string, unknown>) => (item.count || item.cnt || 0) as number))

  return (
    <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
      {data.map((item: Record<string, unknown>, index: number) => {
        const region = (item.region || item.area || '') as string
        const count = (item.count || item.cnt || 0) as number

        return (
          <div key={index} className="rounded-2xl border border-border/60 bg-card p-4 shadow-sm transition-all hover:shadow-md">
            <div className="flex items-center justify-between mb-3">
              <span className="text-sm font-bold text-card-foreground">{region}</span>
              <span className="rounded-full bg-primary/10 px-2.5 py-0.5 text-xs font-semibold text-primary">{count.toLocaleString()}</span>
            </div>
            <div className="h-2 w-full overflow-hidden rounded-full bg-muted">
              <div
                className="h-full rounded-full bg-gradient-to-r from-primary to-coral/60 transition-all duration-500"
                style={{ width: `${(count / maxCount) * 100}%` }}
              />
            </div>
          </div>
        )
      })}
    </div>
  )
}

function GenreSection() {
  const { data, isLoading } = usePopularityByGenreRegion()

  if (isLoading) return <LoadingState />
  if (!data || !Array.isArray(data)) return null

  return (
    <div className="overflow-hidden rounded-2xl border border-border/60 bg-card shadow-sm">
      <table className="w-full text-sm">
        <thead>
          <tr className="border-b border-border/60 bg-muted/30">
            <th className="px-5 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-muted-foreground">장르</th>
            <th className="px-5 py-3.5 text-left text-xs font-semibold uppercase tracking-wider text-muted-foreground">지역</th>
            <th className="px-5 py-3.5 text-right text-xs font-semibold uppercase tracking-wider text-muted-foreground">인기도</th>
          </tr>
        </thead>
        <tbody>
          {data.map((item: Record<string, unknown>, index: number) => {
            const genre = (item.genre || item.genrenm || '') as string
            const region = (item.region || item.area || '') as string
            const count = (item.count || item.cnt || 0) as number

            return (
              <tr key={index} className="border-b border-border/40 last:border-0 transition-colors hover:bg-primary/5">
                <td className="px-5 py-3.5 font-semibold text-card-foreground">{genre}</td>
                <td className="px-5 py-3.5 text-muted-foreground">{region}</td>
                <td className="px-5 py-3.5 text-right">
                  <span className="rounded-full bg-primary/10 px-2.5 py-0.5 text-xs font-semibold text-primary">
                    {count.toLocaleString()}
                  </span>
                </td>
              </tr>
            )
          })}
        </tbody>
      </table>
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
