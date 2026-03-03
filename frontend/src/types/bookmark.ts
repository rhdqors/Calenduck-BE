import type { Performance } from './performance'

export interface BookmarkResponse {
  message: string
  date: string
  reservationDate: string | null
}

export interface MyBookmark extends Performance {
  reservationDate: string
  content: string
  alarm: string
}

export interface EditBookmarkRequest {
  content: string
  alarm: string
}
