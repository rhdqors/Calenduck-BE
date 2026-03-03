import { useQuery } from '@tanstack/react-query'
import { getAlarms } from '@/api/user'
import ErrorFallback from '@/components/common/ErrorFallback'
import { Bell, Loader2 } from 'lucide-react'

export default function AlarmPage() {
  const {
    data: alarms,
    isLoading,
    isError,
    refetch,
  } = useQuery({
    queryKey: ['alarms'],
    queryFn: getAlarms,
  })

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-2">
        <Bell className="h-5 w-5 text-primary" />
        <h1 className="text-xl font-bold">알람</h1>
      </div>

      {isLoading ? (
        <div className="flex items-center justify-center py-20">
          <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
        </div>
      ) : isError ? (
        <ErrorFallback message="알람을 불러올 수 없습니다." onRetry={() => refetch()} />
      ) : alarms && alarms.length > 0 ? (
        <div className="space-y-3">
          {alarms.map((alarm, index) => (
            <div
              key={index}
              className="rounded-lg border border-border bg-card p-4 text-sm text-card-foreground"
            >
              {typeof alarm === 'string' ? alarm : JSON.stringify(alarm)}
            </div>
          ))}
        </div>
      ) : (
        <div className="flex flex-col items-center justify-center gap-3 py-20">
          <Bell className="h-12 w-12 text-muted-foreground/50" />
          <p className="text-muted-foreground">설정된 알람이 없습니다.</p>
        </div>
      )}
    </div>
  )
}
