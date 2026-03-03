import { useState } from 'react'
import { CalendarDays } from 'lucide-react'
import type { MyBookmark } from '@/types/bookmark'
import { parseDateKey } from '@/hooks/useCalendar'
import BookmarkCard from '@/components/bookmark/BookmarkCard'
import BookmarkEditModal from '@/components/bookmark/BookmarkEditModal'

const DAY_NAMES = ['일', '월', '화', '수', '목', '금', '토']

interface CalendarDayDetailProps {
  date: string
  bookmarks: MyBookmark[]
}

export default function CalendarDayDetail({ date, bookmarks }: CalendarDayDetailProps) {
  const [editTarget, setEditTarget] = useState<MyBookmark | null>(null)
  const { year, month, day } = parseDateKey(date)
  const dayOfWeek = new Date(year, month - 1, day).getDay()
  const label = `${month}월 ${day}일 ${DAY_NAMES[dayOfWeek]}요일`

  return (
    <div className="space-y-3">
      <div className="flex items-center justify-between">
        <h2 className="text-sm font-bold text-foreground">{label}</h2>
        {bookmarks.length > 0 && (
          <span className="rounded-full bg-primary/10 px-2.5 py-0.5 text-xs font-semibold text-primary">
            {bookmarks.length}건
          </span>
        )}
      </div>

      {bookmarks.length > 0 ? (
        <div className="space-y-3">
          {bookmarks.map((bookmark) => (
            <BookmarkCard
              key={`${bookmark.mt20id}-${bookmark.reservationDate}`}
              bookmark={bookmark}
              onEdit={setEditTarget}
            />
          ))}
        </div>
      ) : (
        <div className="flex flex-col items-center justify-center gap-2 rounded-2xl border border-border/60 bg-card py-12 shadow-sm">
          <CalendarDays className="h-10 w-10 text-muted-foreground/40" />
          <p className="text-sm text-muted-foreground">이 날짜에 찜한 공연이 없습니다.</p>
        </div>
      )}

      {editTarget && (
        <BookmarkEditModal bookmark={editTarget} onClose={() => setEditTarget(null)} />
      )}
    </div>
  )
}
