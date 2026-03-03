import { Link } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { Bookmark, Bell, TrendingUp, LogOut } from 'lucide-react'
import SearchBar from '@/components/common/SearchBar'

export default function Header() {
  const { isAuthenticated, logout } = useAuth()

  const KAKAO_CLIENT_ID = import.meta.env.VITE_KAKAO_CLIENT_ID
  const KAKAO_REDIRECT_URI = import.meta.env.VITE_KAKAO_REDIRECT_URI || 'http://localhost:5173/oauth/kakao/callback'
  const kakaoLoginUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_CLIENT_ID}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`

  return (
    <header className="sticky top-0 z-50 border-b border-border bg-background/80 backdrop-blur-sm">
      <div className="mx-auto flex h-16 max-w-6xl items-center justify-between px-4">
        <Link to="/" className="text-xl font-bold tracking-tight text-foreground">
          Calenduck
        </Link>

        <div className="hidden md:block">
          <SearchBar />
        </div>

        <nav className="flex items-center gap-1">
          <Link
            to="/ranking"
            className="rounded-md p-2 text-muted-foreground hover:bg-accent hover:text-foreground transition-colors"
            title="인기 공연"
          >
            <TrendingUp className="h-5 w-5" />
          </Link>

          {isAuthenticated ? (
            <>
              <Link
                to="/bookmarks"
                className="rounded-md p-2 text-muted-foreground hover:bg-accent hover:text-foreground transition-colors"
                title="찜목록"
              >
                <Bookmark className="h-5 w-5" />
              </Link>
              <Link
                to="/alarms"
                className="rounded-md p-2 text-muted-foreground hover:bg-accent hover:text-foreground transition-colors"
                title="알람"
              >
                <Bell className="h-5 w-5" />
              </Link>
              <button
                onClick={logout}
                className="rounded-md p-2 text-muted-foreground hover:bg-accent hover:text-foreground transition-colors"
                title="로그아웃"
              >
                <LogOut className="h-5 w-5" />
              </button>
            </>
          ) : (
            <a
              href={kakaoLoginUrl}
              className="ml-2 rounded-md bg-[#FEE500] px-4 py-2 text-sm font-medium text-[#191919] hover:bg-[#FDD835] transition-colors"
            >
              카카오 로그인
            </a>
          )}
        </nav>
      </div>

      <div className="border-t border-border px-4 py-2 md:hidden">
        <SearchBar />
      </div>
    </header>
  )
}
