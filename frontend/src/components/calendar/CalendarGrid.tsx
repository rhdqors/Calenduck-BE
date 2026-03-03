import type { MyBookmark } from '@/types/bookmark'
import { getDaysInMonth, getFirstDayOfWeek, formatDateKey } from '@/hooks/useCalendar'
import CalendarDayCell from './CalendarDayCell'

const WEEKDAYS = ['일', '월', '화', '수', '목', '금', '토']

interface CalendarGridProps {
  year: number
  month: number
  bookmarksByDate: Map<string, MyBookmark[]>
  selectedDate: string | null
  onSelectDate: (dateKey: string) => void
}

export default function CalendarGrid({
  year,
  month,
  bookmarksByDate,
  selectedDate,
  onSelectDate,
}: CalendarGridProps) {
  const daysInMonth = getDaysInMonth(year, month)
  const firstDay = getFirstDayOfWeek(year, month)

  const today = new Date()
  const todayKey = formatDateKey(today.getFullYear(), today.getMonth() + 1, today.getDate())

  return (
    <div className="rounded-2xl border border-border/60 bg-card p-4 shadow-sm">
      <div className="grid grid-cols-7 gap-1">
        {WEEKDAYS.map((day, i) => (
          <div
            key={day}
            className={`py-2 text-center text-xs font-semibold ${
              i === 0 ? 'text-coral' : i === 6 ? 'text-primary' : 'text-muted-foreground'
            }`}
          >
            {day}
          </div>
        ))}

        {Array.from({ length: firstDay }).map((_, i) => (
          <div key={`empty-${i}`} />
        ))}

        {Array.from({ length: daysInMonth }).map((_, i) => {
          const day = i + 1
          const dateKey = formatDateKey(year, month, day)
          const dayOfWeek = (firstDay + i) % 7
          const bookmarks = bookmarksByDate.get(dateKey)

          return (
            <CalendarDayCell
              key={day}
              day={day}
              isToday={dateKey === todayKey}
              isSelected={dateKey === selectedDate}
              hasBookmarks={!!bookmarks}
              bookmarkCount={bookmarks?.length ?? 0}
              dayOfWeek={dayOfWeek}
              onClick={() => onSelectDate(dateKey)}
            />
          )
        })}
      </div>
    </div>
  )
}
