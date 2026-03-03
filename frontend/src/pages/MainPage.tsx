import { usePerformances } from '@/hooks/usePerformances'
import { useSearchRank } from '@/hooks/useSearchRank'
import PerformanceGrid from '@/components/performance/PerformanceGrid'
import ErrorFallback from '@/components/common/ErrorFallback'
import { SkeletonGrid } from '@/components/common/SkeletonCard'
import { TrendingUp } from 'lucide-react'

export default function MainPage() {
  const { data: performances, isLoading, isError, refetch } = usePerformances()
  const { data: searchRanks } = useSearchRank()

  return (
    <div className="space-y-8">
      {searchRanks && searchRanks.length > 0 && (
        <section>
          <div className="flex items-center gap-2 mb-3">
            <TrendingUp className="h-5 w-5 text-primary" />
            <h2 className="text-lg font-semibold">인기 검색어</h2>
          </div>
          <div className="flex flex-wrap gap-2">
            {searchRanks.map((item, index) => (
              <span
                key={item.rankKeyword}
                className="inline-flex items-center gap-1.5 rounded-full border border-border bg-card px-3 py-1.5 text-sm transition-colors hover:bg-accent cursor-default"
              >
                <span className="font-bold text-primary">{index + 1}</span>
                <span className="text-card-foreground">{item.rankKeyword}</span>
              </span>
            ))}
          </div>
        </section>
      )}

      <section>
        <h2 className="mb-4 text-lg font-semibold">전체 공연</h2>
        {isLoading ? (
          <SkeletonGrid />
        ) : isError ? (
          <ErrorFallback message="공연 목록을 불러올 수 없습니다." onRetry={() => refetch()} />
        ) : (
          <PerformanceGrid performances={performances ?? []} />
        )}
      </section>
    </div>
  )
}
