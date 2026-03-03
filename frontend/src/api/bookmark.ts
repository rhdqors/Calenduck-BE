import client from './client'
import type { ApiResponse } from '@/types/api'
import type { BookmarkResponse, MyBookmark, EditBookmarkRequest } from '@/types/bookmark'

export async function toggleBookmark(mt20id: string, year: number, month: number, day: number) {
  const { data } = await client.post<ApiResponse<BookmarkResponse>>(
    `/performances/${mt20id}/bookmark/${year}/${month}/${day}`,
  )
  return data.data
}

export async function getBookmarks() {
  const { data } = await client.get<ApiResponse<MyBookmark[]>>('/performances/bookmark')
  return data.data
}

export async function editBookmark(
  mt20id: string,
  year: number,
  month: number,
  day: number,
  body: EditBookmarkRequest,
) {
  const { data } = await client.patch<ApiResponse<string>>(
    `/performances/${mt20id}/bookmark/${year}/${month}/${day}`,
    body,
  )
  return data
}
