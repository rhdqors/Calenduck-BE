import { useState } from 'react'
import { useBookmarks } from '@/hooks/useBookmarks'
import BookmarkCard from '@/components/bookmark/BookmarkCard'
import BookmarkEditModal from '@/components/bookmark/BookmarkEditModal'
import ErrorFallback from '@/components/common/ErrorFallback'
import type { MyBookmark } from '@/types/bookmark'
import { Bookmark, Loader2 } from 'lucide-react'

export default function BookmarkPage() {
  const { data: bookmarks, isLoading, isError, refetch } = useBookmarks()
  const [editTarget, setEditTarget] = useState<MyBookmark | null>(null)

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-2">
        <Bookmark className="h-5 w-5 text-primary" />
        <h1 className="text-xl font-bold">찜목록</h1>
      </div>

      {isLoading ? (
        <div className="flex items-center justify-center py-20">
          <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
        </div>
      ) : isError ? (
        <ErrorFallback message="찜목록을 불러올 수 없습니다." onRetry={() => refetch()} />
      ) : bookmarks && bookmarks.length > 0 ? (
        <div className="grid gap-4 md:grid-cols-2">
          {bookmarks.map((bookmark) => (
            <BookmarkCard
              key={`${bookmark.mt20id}-${bookmark.reservationDate}`}
              bookmark={bookmark}
              onEdit={setEditTarget}
            />
          ))}
        </div>
      ) : (
        <div className="flex flex-col items-center justify-center gap-3 py-20">
          <Bookmark className="h-12 w-12 text-muted-foreground/50" />
          <p className="text-muted-foreground">찜한 공연이 없습니다.</p>
        </div>
      )}

      {editTarget && (
        <BookmarkEditModal bookmark={editTarget} onClose={() => setEditTarget(null)} />
      )}
    </div>
  )
}
