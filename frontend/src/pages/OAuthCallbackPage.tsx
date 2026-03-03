import { useEffect, useRef } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { kakaoLogin } from '@/api/user'
import { Loader2 } from 'lucide-react'

export default function OAuthCallbackPage() {
  const [searchParams] = useSearchParams()
  const navigate = useNavigate()
  const { login } = useAuth()
  const processed = useRef(false)

  useEffect(() => {
    if (processed.current) return
    processed.current = true

    const code = searchParams.get('code')
    if (!code) {
      navigate('/', { replace: true })
      return
    }

    kakaoLogin(code)
      .then((token) => {
        if (token) {
          login(token)
        }
        navigate('/', { replace: true })
      })
      .catch(() => {
        navigate('/', { replace: true })
      })
  }, [searchParams, navigate, login])

  return (
    <div className="flex flex-col items-center justify-center gap-3 py-20">
      <Loader2 className="h-8 w-8 animate-spin text-primary" />
      <p className="text-sm text-muted-foreground">로그인 처리 중...</p>
    </div>
  )
}
