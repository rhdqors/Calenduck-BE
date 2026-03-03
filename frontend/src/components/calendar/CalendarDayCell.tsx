interface CalendarDayCellProps {
  day: number
  isToday: boolean
  isSelected: boolean
  hasBookmarks: boolean
  bookmarkCount: number
  dayOfWeek: number
  onClick: () => void
}

export default function CalendarDayCell({
  day,
  isToday,
  isSelected,
  hasBookmarks,
  bookmarkCount,
  dayOfWeek,
  onClick,
}: CalendarDayCellProps) {
  const isSunday = dayOfWeek === 0
  const isSaturday = dayOfWeek === 6

  const baseClass = 'flex aspect-square flex-col items-center justify-center gap-1 rounded-xl text-sm font-medium cursor-pointer transition-all'

  let stateClass: string
  if (isToday && isSelected) {
    stateClass = 'bg-gradient-to-br from-primary to-coral text-white shadow-md ring-2 ring-primary/30'
  } else if (isToday) {
    stateClass = 'bg-gradient-to-br from-primary to-coral text-white shadow-sm'
  } else if (isSelected) {
    stateClass = 'bg-primary/10 text-primary ring-2 ring-primary/30'
  } else {
    const weekendColor = isSunday ? 'text-coral' : isSaturday ? 'text-primary' : 'text-foreground'
    stateClass = `${weekendColor} hover:bg-primary/5`
  }

  const dotCount = Math.min(bookmarkCount, 3)

  return (
    <button className={`${baseClass} ${stateClass}`} onClick={onClick}>
      <span>{day}</span>
      {hasBookmarks && (
        <div className="flex gap-0.5">
          {Array.from({ length: dotCount }).map((_, i) => (
            <span
              key={i}
              className={`h-1.5 w-1.5 rounded-full ${
                isToday ? 'bg-white/80' : 'bg-primary'
              }`}
            />
          ))}
        </div>
      )}
    </button>
  )
}
