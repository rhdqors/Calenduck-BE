import { useQuery } from '@tanstack/react-query'
import { getPerformances } from '@/api/performance'
import { mockPerformances } from '@/mocks/performances'

const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true'

export function usePerformances(prfnm?: string, prfcast?: string) {
  return useQuery({
    queryKey: ['performances', prfnm, prfcast],
    queryFn: async () => {
      if (USE_MOCK) return mockPerformances
      return await getPerformances(prfnm, prfcast)
    },
    staleTime: 1000 * 60 * 10,
  })
}
