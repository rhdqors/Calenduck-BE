import client from './client'
import type { ApiResponse } from '@/types/api'
import type { Performance, SearchRank, RankingCount } from '@/types/performance'

export async function getPerformances(prfnm?: string, prfcast?: string) {
  const params = new URLSearchParams()
  if (prfnm) params.set('prfnm', prfnm)
  if (prfcast) params.set('prfcast', prfcast)
  const query = params.toString()
  const url = query ? `/performances?${query}` : '/performances'
  const { data } = await client.get<ApiResponse<Performance[]>>(url)
  return data.data
}

export async function getPerformanceById(mt20id: string) {
  const { data } = await client.get<ApiResponse<Performance>>(`/performances/${mt20id}`)
  return data.data
}

export async function getSearchRank() {
  const { data } = await client.get<ApiResponse<SearchRank[]>>('/performances/search/rank')
  return data.data
}

export async function getTopTen() {
  const { data } = await client.get<ApiResponse<RankingCount[]>>('/performances/topten')
  return data.data
}

export async function getPopularityByRegion() {
  const { data } = await client.get<ApiResponse<RankingCount[]>>('/performances/popularity/region')
  return data.data
}

export async function getPopularityByGenreRegion() {
  const { data } = await client.get<ApiResponse<RankingCount[]>>('/performances/popularity/genres/region')
  return data.data
}
