import { useQuery } from '@tanstack/react-query'
import { getTopTen, getPopularityByRegion, getPopularityByGenreRegion } from '@/api/performance'
import { mockTopTen, mockPopularityByRegion, mockPopularityByGenreRegion } from '@/mocks/rankings'

const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true'

export function useTopTen() {
  return useQuery({
    queryKey: ['topTen'],
    queryFn: async () => {
      if (USE_MOCK) return mockTopTen
      try {
        const data = await getTopTen()
        return data || mockTopTen
      } catch {
        return mockTopTen
      }
    },
    staleTime: 1000 * 60 * 30,
  })
}

export function usePopularityByRegion() {
  return useQuery({
    queryKey: ['popularityByRegion'],
    queryFn: async () => {
      if (USE_MOCK) return mockPopularityByRegion
      try {
        const data = await getPopularityByRegion()
        return data || mockPopularityByRegion
      } catch {
        return mockPopularityByRegion
      }
    },
    staleTime: 1000 * 60 * 30,
  })
}

export function usePopularityByGenreRegion() {
  return useQuery({
    queryKey: ['popularityByGenreRegion'],
    queryFn: async () => {
      if (USE_MOCK) return mockPopularityByGenreRegion
      try {
        const data = await getPopularityByGenreRegion()
        return data || mockPopularityByGenreRegion
      } catch {
        return mockPopularityByGenreRegion
      }
    },
    staleTime: 1000 * 60 * 30,
  })
}
