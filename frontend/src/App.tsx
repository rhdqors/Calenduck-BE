import { BrowserRouter, Routes, Route } from 'react-router-dom'
import MainPage from '@/pages/MainPage'
import SearchPage from '@/pages/SearchPage'
import PerformanceDetailPage from '@/pages/PerformanceDetailPage'
import RankingPage from '@/pages/RankingPage'
import BookmarkPage from '@/pages/BookmarkPage'
import AlarmPage from '@/pages/AlarmPage'
import OAuthCallbackPage from '@/pages/OAuthCallbackPage'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="/performances/:mt20id" element={<PerformanceDetailPage />} />
        <Route path="/ranking" element={<RankingPage />} />
        <Route path="/bookmarks" element={<BookmarkPage />} />
        <Route path="/alarms" element={<AlarmPage />} />
        <Route path="/oauth/kakao/callback" element={<OAuthCallbackPage />} />
      </Routes>
    </BrowserRouter>
  )
}
