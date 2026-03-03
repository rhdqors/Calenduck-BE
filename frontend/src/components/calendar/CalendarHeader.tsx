import { ChevronLeft, ChevronRight, CalendarDays } from 'lucide-react'

interface CalendarHeaderProps {
  year: number
  month: number
  onPrevMonth: () => void
  onNextMonth: () => void
  onToday: () => void
}

export default function CalendarHeader({
  year,
  month,
  onPrevMonth,
  onNextMonth,
  onToday,
}: CalendarHeaderProps) {
  return (
    <div className="flex items-center justify-between">
      <div className="flex items-center gap-2">
        <CalendarDays className="h-5 w-5 text-primary" />
        <h1 className="text-xl font-bold">{year}년 {month}월</h1>
      </div>

      <div className="flex items-center gap-1">
        <button
          onClick={onToday}
          className="rounded-lg px-3 py-1.5 text-xs font-semibold text-primary hover:bg-primary/10 transition-colors"
        >
          오늘
        </button>
        <button
          onClick={onPrevMonth}
          className="rounded-lg p-1.5 text-muted-foreground hover:bg-primary/10 hover:text-primary transition-colors"
        >
          <ChevronLeft className="h-5 w-5" />
        </button>
        <button
          onClick={onNextMonth}
          className="rounded-lg p-1.5 text-muted-foreground hover:bg-primary/10 hover:text-primary transition-colors"
        >
          <ChevronRight className="h-5 w-5" />
        </button>
      </div>
    </div>
  )
}
