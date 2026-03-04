import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getBookmarks, toggleBookmark, editBookmark } from '@/api/bookmark'
import { mockBookmarks } from '@/mocks/bookmarks'
import type { EditBookmarkRequest } from '@/types/bookmark'

const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true'

export function useBookmarks() {
  return useQuery({
    queryKey: ['bookmarks'],
    queryFn: async () => {
      if (USE_MOCK) return mockBookmarks
      return await getBookmarks()
    },
  })
}

export function useToggleBookmark() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ mt20id, year, month, day }: { mt20id: string; year: number; month: number; day: number }) =>
      toggleBookmark(mt20id, year, month, day),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['bookmarks'] })
    },
  })
}

export function useEditBookmark() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({
      mt20id,
      year,
      month,
      day,
      body,
    }: {
      mt20id: string
      year: number
      month: number
      day: number
      body: EditBookmarkRequest
    }) => editBookmark(mt20id, year, month, day, body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['bookmarks'] })
    },
  })
}
