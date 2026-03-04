import { useQuery } from '@tanstack/react-query'
import { getPerformances, getPerformanceById } from '@/api/performance'
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

export function usePerformanceDetail(mt20id: string | undefined) {
  return useQuery({
    queryKey: ['performance', mt20id],
    queryFn: async () => {
      if (USE_MOCK) return mockPerformances.find((p) => p.mt20id === mt20id) ?? null
      return await getPerformanceById(mt20id!)
    },
    enabled: !!mt20id,
    staleTime: 1000 * 60 * 10,
  })
}
