export interface Performance {
  mt20id: string
  poster: string
  prfnm: string
  prfcast: string
  genrenm: string
  fcltynm: string
  dtguidance: string
  stdate: string
  eddate: string
  pcseguidance: string
}

export interface SearchRank {
  rankKeyword: string
  rank: number
}

export interface RankingCount {
  id?: string
  name: string
  count: number
}
