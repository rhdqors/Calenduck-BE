import { Link } from 'react-router-dom'
import { Calendar, MapPin } from 'lucide-react'
import type { Performance } from '@/types/performance'

function formatDate(dateStr: string) {
  if (dateStr.length !== 8) return dateStr
  return `${dateStr.slice(0, 4)}.${dateStr.slice(4, 6)}.${dateStr.slice(6, 8)}`
}

interface PerformanceCardProps {
  performance: Performance
}

export default function PerformanceCard({ performance }: PerformanceCardProps) {
  const { mt20id, poster, prfnm, genrenm, fcltynm, stdate, eddate } = performance

  return (
    <Link
      to={`/performances/${mt20id}`}
      state={{ performance }}
      className="group block overflow-hidden rounded-xl border border-border bg-card transition-all hover:shadow-lg hover:-translate-y-1"
    >
      <div className="aspect-[3/4] overflow-hidden bg-muted">
        <img
          src={poster}
          alt={prfnm}
          className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-105"
          loading="lazy"
          onError={(e) => {
            e.currentTarget.src = `https://placehold.co/300x400/1a1a2e/e94560?text=${encodeURIComponent(prfnm.slice(0, 4))}`
          }}
        />
      </div>
      <div className="p-3.5">
        <span className="inline-block rounded-full bg-primary/10 px-2.5 py-0.5 text-xs font-medium text-primary">
          {genrenm}
        </span>
        <h3 className="mt-2 line-clamp-1 text-sm font-semibold text-card-foreground">
          {prfnm}
        </h3>
        <div className="mt-2 flex items-center gap-1 text-xs text-muted-foreground">
          <MapPin className="h-3 w-3 shrink-0" />
          <span className="line-clamp-1">{fcltynm}</span>
        </div>
        <div className="mt-1 flex items-center gap-1 text-xs text-muted-foreground">
          <Calendar className="h-3 w-3 shrink-0" />
          <span>{formatDate(stdate)} ~ {formatDate(eddate)}</span>
        </div>
      </div>
    </Link>
  )
}
