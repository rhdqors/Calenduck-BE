import { useMemo } from 'react'
import { useBookmarks } from '@/hooks/useBookmarks'
import { useCalendar } from '@/hooks/useCalendar'
import type { MyBookmark } from '@/types/bookmark'
import CalendarHeader from '@/components/calendar/CalendarHeader'
import CalendarGrid from '@/components/calendar/CalendarGrid'
import CalendarDayDetail from '@/components/calendar/CalendarDayDetail'
import ErrorFallback from '@/components/common/ErrorFallback'
import { Loader2 } from 'lucide-react'

export default function CalendarPage() {
  const { data: bookmarks, isLoading, isError, refetch } = useBookmarks()
  const { year, month, selectedDate, setSelectedDate, goToPrevMonth, goToNextMonth, goToToday } =
    useCalendar()

  const bookmarksByDate = useMemo(() => {
    const map = new Map<string, MyBookmark[]>()
    if (!bookmarks) return map
    for (const bm of bookmarks) {
      const key = bm.reservationDate
      if (!key) continue
      const existing = map.get(key)
      if (existing) {
        existing.push(bm)
      } else {
        map.set(key, [bm])
      }
    }
    return map
  }, [bookmarks])

  const selectedBookmarks = selectedDate ? (bookmarksByDate.get(selectedDate) ?? []) : []

  return (
    <div className="space-y-6">
      <CalendarHeader
        year={year}
        month={month}
        onPrevMonth={goToPrevMonth}
        onNextMonth={goToNextMonth}
        onToday={goToToday}
      />

      {isLoading ? (
        <div className="flex items-center justify-center py-20">
          <Loader2 className="h-8 w-8 animate-spin text-primary" />
        </div>
      ) : isError ? (
        <ErrorFallback message="북마크를 불러올 수 없습니다." onRetry={() => refetch()} />
      ) : (
        <>
          <CalendarGrid
            year={year}
            month={month}
            bookmarksByDate={bookmarksByDate}
            selectedDate={selectedDate}
            onSelectDate={setSelectedDate}
          />
          {selectedDate && (
            <CalendarDayDetail date={selectedDate} bookmarks={selectedBookmarks} />
          )}
        </>
      )}
    </div>
  )
}
