import type { RankingCount } from '@/types/performance'

export const mockTopTen: RankingCount[] = [
  { name: '오페라의 유령', count: 1520 },
  { name: '위키드', count: 1340 },
  { name: '햄릿', count: 1120 },
  { name: '백조의 호수', count: 980 },
  { name: '서울시향 정기 연주회', count: 870 },
  { name: '시카고', count: 760 },
  { name: '캣츠', count: 650 },
  { name: '레미제라블', count: 540 },
  { name: '맘마미아', count: 430 },
  { name: '킹키부츠', count: 320 },
]

export const mockPopularityByRegion: RankingCount[] = [
  { name: '세종문화회관', count: 52 },
  { name: '예술의전당', count: 38 },
  { name: 'LG아트센터', count: 25 },
  { name: '국립극장', count: 18 },
  { name: '대학로극장', count: 12 },
]

export const mockPopularityByGenreRegion: RankingCount[] = [
  { name: '뮤지컬', count: 45 },
  { name: '연극', count: 32 },
  { name: '콘서트', count: 28 },
  { name: '클래식', count: 15 },
  { name: '무용', count: 8 },
]
