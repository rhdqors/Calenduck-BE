import { useSearchParams } from 'react-router-dom'
import { usePerformances } from '@/hooks/usePerformances'
import PerformanceGrid from '@/components/performance/PerformanceGrid'
import ErrorFallback from '@/components/common/ErrorFallback'
import SearchBar from '@/components/common/SearchBar'
import { Loader2, SearchX } from 'lucide-react'

export default function SearchPage() {
  const [searchParams] = useSearchParams()
  const query = searchParams.get('query') || ''

  const { data: performances, isLoading, isError, refetch } = usePerformances(query, query)

  return (
    <div className="space-y-6">
      <div className="flex flex-col items-center gap-4 py-4">
        <SearchBar defaultValue={query} />
        {query && (
          <p className="text-sm text-muted-foreground">
            <span className="font-medium text-foreground">"{query}"</span> 검색 결과
          </p>
        )}
      </div>

      {isLoading ? (
        <div className="flex items-center justify-center py-20">
          <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
        </div>
      ) : isError ? (
        <ErrorFallback message="검색 결과를 불러올 수 없습니다." onRetry={() => refetch()} />
      ) : performances && performances.length > 0 ? (
        <PerformanceGrid performances={performances} />
      ) : (
        <div className="flex flex-col items-center justify-center gap-3 py-20">
          <SearchX className="h-12 w-12 text-muted-foreground/50" />
          <p className="text-muted-foreground">검색 결과가 없습니다.</p>
        </div>
      )}
    </div>
  )
}
