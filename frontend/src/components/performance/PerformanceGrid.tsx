import type { Performance } from '@/types/performance'
import PerformanceCard from './PerformanceCard'

interface PerformanceGridProps {
  performances: Performance[]
}

export default function PerformanceGrid({ performances }: PerformanceGridProps) {
  if (performances.length === 0) {
    return (
      <div className="flex items-center justify-center py-20">
        <p className="text-muted-foreground">등록된 공연이 없습니다.</p>
      </div>
    )
  }

  return (
    <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5">
      {performances.map((performance) => (
        <PerformanceCard key={performance.mt20id} performance={performance} />
      ))}
    </div>
  )
}
