import { useLocation, useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import type { Performance } from '@/types/performance'
import { ArrowLeft, Calendar, MapPin, Clock, Users, CreditCard, Bookmark } from 'lucide-react'
import { useState } from 'react'
import { toggleBookmark } from '@/api/bookmark'

function formatDate(dateStr: string) {
  if (dateStr.length !== 8) return dateStr
  return `${dateStr.slice(0, 4)}.${dateStr.slice(4, 6)}.${dateStr.slice(6, 8)}`
}

export default function PerformanceDetailPage() {
  const { mt20id } = useParams<{ mt20id: string }>()
  const location = useLocation()
  const navigate = useNavigate()
  const { isAuthenticated } = useAuth()
  const [bookmarkMessage, setBookmarkMessage] = useState<string | null>(null)

  const performance = (location.state as { performance?: Performance })?.performance

  if (!performance) {
    return (
      <div className="flex flex-col items-center justify-center gap-4 py-20">
        <p className="text-muted-foreground">공연 정보를 찾을 수 없습니다.</p>
        <button
          onClick={() => navigate('/')}
          className="rounded-md bg-primary px-4 py-2 text-sm text-primary-foreground hover:bg-primary/90 transition-colors"
        >
          메인으로 돌아가기
        </button>
      </div>
    )
  }

  const { poster, prfnm, prfcast, genrenm, fcltynm, dtguidance, stdate, eddate, pcseguidance } =
    performance

  const handleBookmark = async () => {
    if (!isAuthenticated) {
      setBookmarkMessage('로그인이 필요합니다.')
      return
    }
    try {
      const today = new Date()
      const result = await toggleBookmark(
        mt20id!,
        today.getFullYear(),
        today.getMonth() + 1,
        today.getDate(),
      )
      setBookmarkMessage(result.message)
    } catch {
      setBookmarkMessage('찜하기에 실패했습니다.')
    }
  }

  return (
    <div className="space-y-6">
      <button
        onClick={() => navigate(-1)}
        className="inline-flex items-center gap-1 text-sm text-muted-foreground hover:text-foreground transition-colors"
      >
        <ArrowLeft className="h-4 w-4" />
        뒤로가기
      </button>

      <div className="flex flex-col gap-8 md:flex-row">
        <div className="w-full shrink-0 md:w-72">
          <div className="overflow-hidden rounded-xl border border-border">
            <img
              src={poster}
              alt={prfnm}
              className="h-auto w-full object-cover"
              onError={(e) => {
                e.currentTarget.src = `https://placehold.co/300x400/1a1a2e/e94560?text=${encodeURIComponent(prfnm.slice(0, 4))}`
              }}
            />
          </div>
        </div>

        <div className="flex-1 space-y-5">
          <div>
            <span className="inline-block rounded-full bg-primary/10 px-3 py-1 text-sm font-medium text-primary">
              {genrenm}
            </span>
            <h1 className="mt-3 text-2xl font-bold text-foreground">{prfnm}</h1>
          </div>

          <div className="space-y-3 rounded-lg border border-border bg-card p-4">
            <div className="flex items-start gap-3">
              <MapPin className="mt-0.5 h-4 w-4 shrink-0 text-muted-foreground" />
              <div>
                <p className="text-xs text-muted-foreground">장소</p>
                <p className="text-sm font-medium">{fcltynm}</p>
              </div>
            </div>
            <div className="flex items-start gap-3">
              <Calendar className="mt-0.5 h-4 w-4 shrink-0 text-muted-foreground" />
              <div>
                <p className="text-xs text-muted-foreground">기간</p>
                <p className="text-sm font-medium">
                  {formatDate(stdate)} ~ {formatDate(eddate)}
                </p>
              </div>
            </div>
            <div className="flex items-start gap-3">
              <Clock className="mt-0.5 h-4 w-4 shrink-0 text-muted-foreground" />
              <div>
                <p className="text-xs text-muted-foreground">공연시간</p>
                <p className="text-sm font-medium">{dtguidance}</p>
              </div>
            </div>
            <div className="flex items-start gap-3">
              <Users className="mt-0.5 h-4 w-4 shrink-0 text-muted-foreground" />
              <div>
                <p className="text-xs text-muted-foreground">출연진</p>
                <p className="text-sm font-medium">{prfcast}</p>
              </div>
            </div>
            <div className="flex items-start gap-3">
              <CreditCard className="mt-0.5 h-4 w-4 shrink-0 text-muted-foreground" />
              <div>
                <p className="text-xs text-muted-foreground">티켓 가격</p>
                <p className="text-sm font-medium">{pcseguidance}</p>
              </div>
            </div>
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={handleBookmark}
              className="inline-flex items-center gap-2 rounded-lg bg-primary px-5 py-2.5 text-sm font-medium text-primary-foreground hover:bg-primary/90 transition-colors"
            >
              <Bookmark className="h-4 w-4" />
              찜하기
            </button>
            {bookmarkMessage && (
              <span className="text-sm text-muted-foreground">{bookmarkMessage}</span>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
