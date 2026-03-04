import { useQuery } from '@tanstack/react-query'
import { getTopTen, getPopularityByRegion, getPopularityByGenreRegion } from '@/api/performance'
import { mockTopTen, mockPopularityByRegion, mockPopularityByGenreRegion } from '@/mocks/rankings'
import type { RankingCount } from '@/types/performance'

const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true'

export function useTopTen() {
  return useQuery<RankingCount[]>({
    queryKey: ['topTen'],
    queryFn: async () => {
      if (USE_MOCK) return mockTopTen
      return await getTopTen()
    },
    staleTime: 1000 * 60 * 30,
  })
}

export function usePopularityByRegion() {
  return useQuery<RankingCount[]>({
    queryKey: ['popularityByRegion'],
    queryFn: async () => {
      if (USE_MOCK) return mockPopularityByRegion
      return await getPopularityByRegion()
    },
    staleTime: 1000 * 60 * 30,
  })
}

export function usePopularityByGenreRegion() {
  return useQuery<RankingCount[]>({
    queryKey: ['popularityByGenreRegion'],
    queryFn: async () => {
      if (USE_MOCK) return mockPopularityByGenreRegion
      return await getPopularityByGenreRegion()
    },
    staleTime: 1000 * 60 * 30,
  })
}
