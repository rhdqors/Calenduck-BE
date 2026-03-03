import { Link } from 'react-router-dom'
import { Calendar, MapPin, FileText, Bell, Pencil } from 'lucide-react'
import type { MyBookmark } from '@/types/bookmark'

function formatDate(dateStr: string) {
  if (dateStr.length !== 8) return dateStr
  return `${dateStr.slice(0, 4)}.${dateStr.slice(4, 6)}.${dateStr.slice(6, 8)}`
}

interface BookmarkCardProps {
  bookmark: MyBookmark
  onEdit: (bookmark: MyBookmark) => void
}

export default function BookmarkCard({ bookmark, onEdit }: BookmarkCardProps) {
  const {
    mt20id,
    poster,
    prfnm,
    genrenm,
    fcltynm,
    stdate,
    eddate,
    reservationDate,
    content,
    alarm,
  } = bookmark

  return (
    <div className="flex gap-4 rounded-xl border border-border bg-card p-4 transition-all hover:shadow-md">
      <Link
        to={`/performances/${mt20id}`}
        state={{ performance: bookmark }}
        className="shrink-0"
      >
        <div className="h-32 w-24 overflow-hidden rounded-lg bg-muted">
          <img
            src={poster}
            alt={prfnm}
            className="h-full w-full object-cover"
            loading="lazy"
            onError={(e) => {
              e.currentTarget.src = `https://placehold.co/96x128/1a1a2e/e94560?text=${encodeURIComponent(prfnm.slice(0, 2))}`
            }}
          />
        </div>
      </Link>

      <div className="flex flex-1 flex-col justify-between">
        <div className="space-y-1.5">
          <div className="flex items-start justify-between gap-2">
            <div>
              <span className="inline-block rounded-full bg-primary/10 px-2 py-0.5 text-xs font-medium text-primary">
                {genrenm}
              </span>
              <h3 className="mt-1 text-sm font-semibold text-card-foreground line-clamp-1">
                {prfnm}
              </h3>
            </div>
            <button
              onClick={() => onEdit(bookmark)}
              className="shrink-0 rounded-md p-1.5 text-muted-foreground hover:bg-accent hover:text-foreground transition-colors"
              title="수정"
            >
              <Pencil className="h-4 w-4" />
            </button>
          </div>

          <div className="flex items-center gap-1 text-xs text-muted-foreground">
            <MapPin className="h-3 w-3 shrink-0" />
            <span className="line-clamp-1">{fcltynm}</span>
          </div>
          <div className="flex items-center gap-1 text-xs text-muted-foreground">
            <Calendar className="h-3 w-3 shrink-0" />
            <span>{formatDate(stdate)} ~ {formatDate(eddate)}</span>
          </div>
        </div>

        <div className="mt-2 flex flex-wrap items-center gap-3 text-xs text-muted-foreground">
          {reservationDate && (
            <span className="inline-flex items-center gap-1 rounded-md bg-accent px-2 py-0.5">
              <Calendar className="h-3 w-3" />
              예약: {formatDate(reservationDate)}
            </span>
          )}
          {content && (
            <span className="inline-flex items-center gap-1" title={content}>
              <FileText className="h-3 w-3" />
              메모
            </span>
          )}
          {alarm && (
            <span className="inline-flex items-center gap-1">
              <Bell className="h-3 w-3" />
              알람 설정됨
            </span>
          )}
        </div>
      </div>
    </div>
  )
}
