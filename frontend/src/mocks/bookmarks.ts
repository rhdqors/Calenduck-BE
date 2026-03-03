import type { MyBookmark } from '@/types/bookmark'

export const mockBookmarks: MyBookmark[] = [
  {
    mt20id: 'PF001',
    poster: 'https://placehold.co/300x400/1a1a2e/e94560?text=Musical',
    prfnm: '오페라의 유령',
    prfcast: '홍길동, 김영희',
    genrenm: '뮤지컬',
    fcltynm: '세종문화회관',
    dtguidance: '화요일~금요일(19:30), 토요일(14:00, 19:00), 일요일(14:00)',
    stdate: '20260301',
    eddate: '20260630',
    pcseguidance: 'VIP석 150,000원, R석 120,000원, S석 90,000원',
    reservationDate: '20260315',
    content: '생일 선물로 예매',
    alarm: '20260314,20260312',
  },
  {
    mt20id: 'PF003',
    poster: 'https://placehold.co/300x400/1b1a2e/e94560?text=Play',
    prfnm: '햄릿',
    prfcast: '조승우, 김다미',
    genrenm: '연극',
    fcltynm: '국립극장 해오름극장',
    dtguidance: '화~금(19:30), 토(15:00, 19:30), 일(15:00)',
    stdate: '20260401',
    eddate: '20260531',
    pcseguidance: 'R석 70,000원, S석 50,000원',
    reservationDate: '20260405',
    content: '',
    alarm: '',
  },
]
