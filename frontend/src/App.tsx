import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider } from '@/contexts/AuthContext'
import Layout from '@/components/layout/Layout'
import ErrorBoundary from '@/components/common/ErrorBoundary'
import ProtectedRoute from '@/components/common/ProtectedRoute'
import MainPage from '@/pages/MainPage'
import SearchPage from '@/pages/SearchPage'
import PerformanceDetailPage from '@/pages/PerformanceDetailPage'
import RankingPage from '@/pages/RankingPage'
import BookmarkPage from '@/pages/BookmarkPage'
import CalendarPage from '@/pages/CalendarPage'
import AlarmPage from '@/pages/AlarmPage'
import OAuthCallbackPage from '@/pages/OAuthCallbackPage'

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <ErrorBoundary>
          <Layout>
            <Routes>
            <Route path="/" element={<MainPage />} />
            <Route path="/search" element={<SearchPage />} />
            <Route path="/performances/:mt20id" element={<PerformanceDetailPage />} />
            <Route path="/ranking" element={<RankingPage />} />
            <Route
              path="/bookmarks"
              element={
                <ProtectedRoute>
                  <BookmarkPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/calendar"
              element={
                <ProtectedRoute>
                  <CalendarPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/alarms"
              element={
                <ProtectedRoute>
                  <AlarmPage />
                </ProtectedRoute>
              }
            />
            <Route path="/oauth/kakao/callback" element={<OAuthCallbackPage />} />
            </Routes>
          </Layout>
        </ErrorBoundary>
      </AuthProvider>
    </BrowserRouter>
  )
}
