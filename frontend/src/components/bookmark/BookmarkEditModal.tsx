import { useState } from 'react'
import { X } from 'lucide-react'
import type { MyBookmark } from '@/types/bookmark'
import { useEditBookmark } from '@/hooks/useBookmarks'

interface BookmarkEditModalProps {
  bookmark: MyBookmark
  onClose: () => void
}

function parseReservationDate(dateStr: string) {
  if (dateStr.length !== 8) return { year: 0, month: 0, day: 0 }
  return {
    year: parseInt(dateStr.slice(0, 4)),
    month: parseInt(dateStr.slice(4, 6)),
    day: parseInt(dateStr.slice(6, 8)),
  }
}

export default function BookmarkEditModal({ bookmark, onClose }: BookmarkEditModalProps) {
  const [content, setContent] = useState(bookmark.content)
  const [alarm, setAlarm] = useState(bookmark.alarm)
  const editMutation = useEditBookmark()

  const handleSave = () => {
    const { year, month, day } = parseReservationDate(bookmark.reservationDate)
    if (year === 0) return

    editMutation.mutate(
      {
        mt20id: bookmark.mt20id,
        year,
        month,
        day,
        body: { content, alarm },
      },
      {
        onSuccess: () => onClose(),
      },
    )
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50" onClick={onClose}>
      <div
        className="mx-4 w-full max-w-md rounded-xl border border-border bg-card p-6 shadow-xl"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-lg font-semibold">찜 수정</h2>
          <button
            onClick={onClose}
            className="rounded-md p-1 text-muted-foreground hover:bg-accent hover:text-foreground transition-colors"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        <p className="mb-4 text-sm font-medium text-foreground">{bookmark.prfnm}</p>

        <div className="space-y-4">
          <div>
            <label className="mb-1.5 block text-sm font-medium text-foreground">메모</label>
            <textarea
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="메모를 입력하세요"
              rows={3}
              className="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm outline-none transition-colors focus:border-primary focus:ring-1 focus:ring-ring resize-none"
            />
          </div>

          <div>
            <label className="mb-1.5 block text-sm font-medium text-foreground">
              알람 날짜 (yyyyMMdd, 쉼표 구분)
            </label>
            <input
              type="text"
              value={alarm}
              onChange={(e) => setAlarm(e.target.value)}
              placeholder="20260314,20260312"
              className="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm outline-none transition-colors focus:border-primary focus:ring-1 focus:ring-ring"
            />
          </div>
        </div>

        <div className="mt-6 flex justify-end gap-2">
          <button
            onClick={onClose}
            className="rounded-lg border border-border px-4 py-2 text-sm font-medium text-muted-foreground hover:bg-accent transition-colors"
          >
            취소
          </button>
          <button
            onClick={handleSave}
            disabled={editMutation.isPending}
            className="rounded-lg bg-primary px-4 py-2 text-sm font-medium text-primary-foreground hover:bg-primary/90 transition-colors disabled:opacity-50"
          >
            {editMutation.isPending ? '저장 중...' : '저장'}
          </button>
        </div>
      </div>
    </div>
  )
}
