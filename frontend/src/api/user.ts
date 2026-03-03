import client from './client'
import type { ApiResponse } from '@/types/api'

export async function kakaoLogin(code: string) {
  const response = await client.get<ApiResponse<string>>(`/user/kakao/login?code=${code}`)
  const token = response.headers['authorization']?.replace('Bearer ', '')
  return token || null
}

export async function devLogin() {
  const response = await client.post<ApiResponse<string>>('/dev/login')
  const token = response.headers['authorization']?.replace('Bearer ', '')
  return token || null
}

export async function getAlarms() {
  const { data } = await client.get<ApiResponse<unknown[]>>('/user/alarms')
  return data.data
}
