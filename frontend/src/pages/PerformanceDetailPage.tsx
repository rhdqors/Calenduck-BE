import { useLocation, useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { usePerformanceDetail } from '@/hooks/usePerformances'
import type { Performance } from '@/types/performance'
import ErrorFallback from '@/components/common/ErrorFallback'
import { ArrowLeft, Calendar, MapPin, Clock, Users, CreditCard, Bookmark, Loader2 } from 'lucide-react'
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

  const statePerformance = (location.state as { performance?: Performance })?.performance
  const { data: fetchedPerformance, isLoading, isError, refetch } = usePerformanceDetail(
    statePerformance ? undefined : mt20id,
  )

  const performance = statePerformance ?? fetchedPerformance

  if (isLoading && !statePerformance) {
    return (
      <div className="flex items-center justify-center py-20">
        <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
      </div>
    )
  }

  if (!performance) {
    if (isError) {
      return <ErrorFallback message="공연 정보를 불러올 수 없습니다." onRetry={() => refetch()} />
    }
    return (
      <div className="flex flex-col items-center justify-center gap-4 py-20">
        <p className="text-muted-foreground">공연 정보를 찾을 수 없습니다.</p>
        <button
          onClick={() => navigate('/')}
          className="rounded-full bg-primary px-5 py-2.5 text-sm font-semibold text-primary-foreground shadow-md hover:bg-primary/90 hover:shadow-lg transition-all"
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
        className="inline-flex items-center gap-1 text-sm text-muted-foreground hover:text-primary transition-colors"
      >
        <ArrowLeft className="h-4 w-4" />
        뒤로가기
      </button>

      <div className="flex flex-col gap-8 md:flex-row">
        <div className="w-full shrink-0 md:w-72">
          <div className="overflow-hidden rounded-2xl border border-border/60 shadow-lg">
            <img
              src={poster}
              alt={prfnm}
              className="h-auto w-full object-cover"
              onError={(e) => {
                e.currentTarget.src = `https://placehold.co/300x400/7c3aed/ffffff?text=${encodeURIComponent(prfnm.slice(0, 4))}`
              }}
            />
          </div>
        </div>

        <div className="flex-1 space-y-5">
          <div>
            <span className="inline-block rounded-full bg-gradient-to-r from-primary/15 to-coral/15 px-3 py-1 text-sm font-semibold text-primary">
              {genrenm}
            </span>
            <h1 className="mt-3 text-2xl font-bold text-foreground">{prfnm}</h1>
          </div>

          <div className="space-y-3 rounded-2xl border border-border/60 bg-card p-5 shadow-sm">
            <InfoRow icon={MapPin} label="장소" value={fcltynm} />
            <InfoRow icon={Calendar} label="기간" value={`${formatDate(stdate)} ~ ${formatDate(eddate)}`} />
            <InfoRow icon={Clock} label="공연시간" value={dtguidance} />
            <InfoRow icon={Users} label="출연진" value={prfcast} />
            <InfoRow icon={CreditCard} label="티켓 가격" value={pcseguidance} />
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={handleBookmark}
              className="inline-flex items-center gap-2 rounded-full bg-gradient-to-r from-primary to-primary/80 px-6 py-2.5 text-sm font-semibold text-primary-foreground shadow-md hover:shadow-lg hover:brightness-110 transition-all"
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

function InfoRow({ icon: Icon, label, value }: { icon: typeof MapPin; label: string; value: string }) {
  return (
    <div className="flex items-start gap-3">
      <Icon className="mt-0.5 h-4 w-4 shrink-0 text-primary/60" />
      <div>
        <p className="text-xs text-muted-foreground">{label}</p>
        <p className="text-sm font-medium">{value}</p>
      </div>
    </div>
  )
}
