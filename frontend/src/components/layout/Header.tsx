import { Link } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { Bookmark, Bell, TrendingUp, CalendarDays, LogOut } from 'lucide-react'
import SearchBar from '@/components/common/SearchBar'
import { devLogin } from '@/api/user'

export default function Header() {
  const { isAuthenticated, login, logout } = useAuth()

  const handleDevLogin = async () => {
    const token = await devLogin()
    if (token) login(token)
  }

  const KAKAO_CLIENT_ID = import.meta.env.VITE_KAKAO_CLIENT_ID
  const KAKAO_REDIRECT_URI = import.meta.env.VITE_KAKAO_REDIRECT_URI || 'http://localhost:5173/oauth/kakao/callback'
  const kakaoLoginUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_CLIENT_ID}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`

  return (
    <header className="sticky top-0 z-50 border-b border-border/60 bg-background/80 backdrop-blur-md">
      <div className="mx-auto flex h-16 max-w-6xl items-center justify-between px-4">
        <Link to="/" className="bg-gradient-to-r from-primary to-coral bg-clip-text text-xl font-extrabold tracking-tight text-transparent">
          Calenduck
        </Link>

        <div className="hidden md:block">
          <SearchBar />
        </div>

        <nav className="flex items-center gap-1">
          <Link
            to="/ranking"
            className="rounded-lg p-2 text-muted-foreground hover:bg-primary/10 hover:text-primary transition-colors"
            title="인기 공연"
          >
            <TrendingUp className="h-5 w-5" />
          </Link>

          {isAuthenticated ? (
            <>
              <Link
                to="/calendar"
                className="rounded-lg p-2 text-muted-foreground hover:bg-primary/10 hover:text-primary transition-colors"
                title="캘린더"
              >
                <CalendarDays className="h-5 w-5" />
              </Link>
              <Link
                to="/bookmarks"
                className="rounded-lg p-2 text-muted-foreground hover:bg-primary/10 hover:text-primary transition-colors"
                title="찜목록"
              >
                <Bookmark className="h-5 w-5" />
              </Link>
              <Link
                to="/alarms"
                className="rounded-lg p-2 text-muted-foreground hover:bg-primary/10 hover:text-primary transition-colors"
                title="알람"
              >
                <Bell className="h-5 w-5" />
              </Link>
              <button
                onClick={logout}
                className="rounded-lg p-2 text-muted-foreground hover:bg-primary/10 hover:text-primary transition-colors"
                title="로그아웃"
              >
                <LogOut className="h-5 w-5" />
              </button>
            </>
          ) : (
            <>
              <a
                href={kakaoLoginUrl}
                className="ml-2 rounded-full bg-[#FEE500] px-4 py-2 text-sm font-semibold text-[#191919] shadow-sm hover:bg-[#FDD835] hover:shadow-md transition-all"
              >
                카카오 로그인
              </a>
              {import.meta.env.DEV && (
                <button
                  onClick={handleDevLogin}
                  className="ml-1 rounded-full border border-dashed border-primary/40 px-3 py-2 text-xs font-medium text-primary hover:bg-primary/10 transition-colors"
                >
                  Dev
                </button>
              )}
            </>
          )}
        </nav>
      </div>

      <div className="border-t border-border/60 px-4 py-2 md:hidden">
        <SearchBar />
      </div>
    </header>
  )
}
