interface ErrorFallbackProps {
  message?: string
  onRetry?: () => void
}

export default function ErrorFallback({
  message = '문제가 발생했습니다.',
  onRetry,
}: ErrorFallbackProps) {
  return (
    <div className="flex flex-col items-center justify-center gap-4 py-20">
      <p className="text-muted-foreground">{message}</p>
      {onRetry && (
        <button
          onClick={onRetry}
          className="rounded-md bg-primary px-4 py-2 text-sm text-primary-foreground hover:bg-primary/90 transition-colors"
        >
          다시 시도
        </button>
      )}
    </div>
  )
}
