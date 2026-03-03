import { useQuery } from '@tanstack/react-query'
import { getSearchRank } from '@/api/performance'
import { mockSearchRanks } from '@/mocks/performances'

const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true'

export function useSearchRank() {
  return useQuery({
    queryKey: ['searchRank'],
    queryFn: async () => {
      if (USE_MOCK) return mockSearchRanks
      try {
        const data = await getSearchRank()
        return data.length > 0 ? data : mockSearchRanks
      } catch {
        return mockSearchRanks
      }
    },
    staleTime: 1000 * 60 * 5,
  })
}
