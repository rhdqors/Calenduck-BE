import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { usePerformances } from '@/hooks/usePerformances'
import { useSearchRank } from '@/hooks/useSearchRank'
import PerformanceGrid from '@/components/performance/PerformanceGrid'
import ErrorFallback from '@/components/common/ErrorFallback'
import { SkeletonGrid } from '@/components/common/SkeletonCard'
import { TrendingUp, Sparkles, ChevronDown } from 'lucide-react'

const PAGE_SIZE = 20

export default function MainPage() {
  const navigate = useNavigate()
  const [visibleCount, setVisibleCount] = useState(PAGE_SIZE)
  const { data: performances, isLoading, isError, refetch } = usePerformances()
  const { data: searchRanks } = useSearchRank()

  return (
    <div className="space-y-10">
      <section className="relative overflow-hidden rounded-2xl bg-gradient-to-br from-primary via-primary/80 to-coral p-8 text-white shadow-lg">
        <div className="relative z-10">
          <div className="flex items-center gap-2 mb-2">
            <Sparkles className="h-5 w-5" />
            <span className="text-sm font-medium text-white/80">Calenduck</span>
          </div>
          <h1 className="text-2xl font-bold md:text-3xl">
            오늘의 공연을 발견하세요
          </h1>
          <p className="mt-2 text-sm text-white/70 md:text-base">
            뮤지컬, 연극, 콘서트 등 다양한 공연 정보를 한눈에
          </p>
        </div>
        <div className="absolute -right-8 -top-8 h-40 w-40 rounded-full bg-white/10 blur-2xl" />
        <div className="absolute -bottom-12 -left-12 h-48 w-48 rounded-full bg-coral/20 blur-3xl" />
      </section>

      {searchRanks && searchRanks.length > 0 && (
        <section>
          <div className="flex items-center gap-2 mb-4">
            <TrendingUp className="h-5 w-5 text-coral" />
            <h2 className="text-lg font-bold">인기 검색어</h2>
          </div>
          <div className="flex flex-wrap gap-2">
            {searchRanks.map((item, index) => (
              <button
                key={item.rankKeyword}
                onClick={() => navigate(`/search?query=${encodeURIComponent(item.rankKeyword)}`)}
                className="inline-flex items-center gap-1.5 rounded-full border border-primary/20 bg-primary/5 px-3.5 py-1.5 text-sm transition-all hover:bg-primary/10 hover:shadow-sm cursor-pointer"
              >
                <span className="font-bold text-primary">{index + 1}</span>
                <span className="text-foreground">{item.rankKeyword}</span>
              </button>
            ))}
          </div>
        </section>
      )}

      <section>
        <h2 className="mb-5 text-lg font-bold">전체 공연</h2>
        {isLoading ? (
          <SkeletonGrid />
        ) : isError ? (
          <ErrorFallback message="공연 목록을 불러올 수 없습니다." onRetry={() => refetch()} />
        ) : (
          <>
            <PerformanceGrid performances={(performances ?? []).slice(0, visibleCount)} />
            {performances && visibleCount < performances.length && (
              <div className="mt-8 flex justify-center">
                <button
                  onClick={() => setVisibleCount((prev) => prev + PAGE_SIZE)}
                  className="inline-flex items-center gap-2 rounded-full border border-border bg-card px-6 py-2.5 text-sm font-medium text-foreground shadow-sm transition-all hover:bg-muted hover:shadow-md"
                >
                  <ChevronDown className="h-4 w-4" />
                  더 보기 ({performances.length - visibleCount}개 남음)
                </button>
              </div>
            )}
          </>
        )}
      </section>
    </div>
  )
}
