import { useState } from 'react'

export function getDaysInMonth(year: number, month: number): number {
  return new Date(year, month, 0).getDate()
}

export function getFirstDayOfWeek(year: number, month: number): number {
  return new Date(year, month - 1, 1).getDay()
}

export function formatDateKey(year: number, month: number, day: number): string {
  return `${year}${String(month).padStart(2, '0')}${String(day).padStart(2, '0')}`
}

export function parseDateKey(dateKey: string): { year: number; month: number; day: number } {
  return {
    year: parseInt(dateKey.slice(0, 4)),
    month: parseInt(dateKey.slice(4, 6)),
    day: parseInt(dateKey.slice(6, 8)),
  }
}

export function useCalendar() {
  const today = new Date()
  const [year, setYear] = useState(today.getFullYear())
  const [month, setMonth] = useState(today.getMonth() + 1)
  const [selectedDate, setSelectedDate] = useState<string | null>(null)

  const goToPrevMonth = () => {
    if (month === 1) {
      setYear((y) => y - 1)
      setMonth(12)
    } else {
      setMonth((m) => m - 1)
    }
    setSelectedDate(null)
  }

  const goToNextMonth = () => {
    if (month === 12) {
      setYear((y) => y + 1)
      setMonth(1)
    } else {
      setMonth((m) => m + 1)
    }
    setSelectedDate(null)
  }

  const goToToday = () => {
    const now = new Date()
    setYear(now.getFullYear())
    setMonth(now.getMonth() + 1)
    setSelectedDate(formatDateKey(now.getFullYear(), now.getMonth() + 1, now.getDate()))
  }

  return { year, month, selectedDate, setSelectedDate, goToPrevMonth, goToNextMonth, goToToday }
}
