import { useQuery } from '@tanstack/react-query'
import { getSearchRank } from '@/api/performance'
import { mockSearchRanks } from '@/mocks/performances'

const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true'

export function useSearchRank() {
  return useQuery({
    queryKey: ['searchRank'],
    queryFn: async () => {
      if (USE_MOCK) return mockSearchRanks
      return await getSearchRank()
    },
    staleTime: 1000 * 60 * 5,
  })
}
