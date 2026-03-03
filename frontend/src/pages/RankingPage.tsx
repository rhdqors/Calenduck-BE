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
        <TrendingUp className="h-5 w-5 text-primary" />
        <h1 className="text-xl font-bold">인기 공연</h1>
      </div>

      <div className="flex gap-1 rounded-lg border border-border bg-muted p-1">
        {tabs.map(({ key, label, icon: Icon }) => (
          <button
            key={key}
            onClick={() => setActiveTab(key)}
            className={`flex flex-1 items-center justify-center gap-1.5 rounded-md px-3 py-2 text-sm font-medium transition-colors ${
              activeTab === key
                ? 'bg-background text-foreground shadow-sm'
                : 'text-muted-foreground hover:text-foreground'
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
    <div className="space-y-2">
      {data.map((item: Record<string, unknown>, index: number) => {
        const name = (item.name || item.prfnm || '') as string
        const count = (item.count || item.cnt || 0) as number
        const rank = index + 1
        const maxCount = (data[0] as Record<string, unknown>)?.count as number || (data[0] as Record<string, unknown>)?.cnt as number || 1

        return (
          <div
            key={index}
            className="flex items-center gap-4 rounded-lg border border-border bg-card p-4"
          >
            <span
              className={`flex h-8 w-8 shrink-0 items-center justify-center rounded-full text-sm font-bold ${
                rank <= 3
                  ? 'bg-primary text-primary-foreground'
                  : 'bg-muted text-muted-foreground'
              }`}
            >
              {rank}
            </span>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-card-foreground truncate">{name}</p>
              <div className="mt-1.5 h-2 w-full overflow-hidden rounded-full bg-muted">
                <div
                  className="h-full rounded-full bg-primary/70 transition-all duration-500"
                  style={{ width: `${(count / maxCount) * 100}%` }}
                />
              </div>
            </div>
            <span className="shrink-0 text-xs text-muted-foreground">{count.toLocaleString()}</span>
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
          <div key={index} className="rounded-lg border border-border bg-card p-4">
            <div className="flex items-center justify-between mb-2">
              <span className="text-sm font-semibold text-card-foreground">{region}</span>
              <span className="text-xs text-muted-foreground">{count.toLocaleString()}</span>
            </div>
            <div className="h-2 w-full overflow-hidden rounded-full bg-muted">
              <div
                className="h-full rounded-full bg-primary/70 transition-all duration-500"
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
    <div className="overflow-x-auto">
      <table className="w-full text-sm">
        <thead>
          <tr className="border-b border-border">
            <th className="px-4 py-3 text-left font-medium text-muted-foreground">장르</th>
            <th className="px-4 py-3 text-left font-medium text-muted-foreground">지역</th>
            <th className="px-4 py-3 text-right font-medium text-muted-foreground">인기도</th>
          </tr>
        </thead>
        <tbody>
          {data.map((item: Record<string, unknown>, index: number) => {
            const genre = (item.genre || item.genrenm || '') as string
            const region = (item.region || item.area || '') as string
            const count = (item.count || item.cnt || 0) as number

            return (
              <tr key={index} className="border-b border-border last:border-0">
                <td className="px-4 py-3 font-medium text-card-foreground">{genre}</td>
                <td className="px-4 py-3 text-muted-foreground">{region}</td>
                <td className="px-4 py-3 text-right text-muted-foreground">{count.toLocaleString()}</td>
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
      <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
    </div>
  )
}
