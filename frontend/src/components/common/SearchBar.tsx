import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { Search } from 'lucide-react'

interface SearchBarProps {
  defaultValue?: string
  placeholder?: string
}

export default function SearchBar({
  defaultValue = '',
  placeholder = '공연명 또는 출연진 검색',
}: SearchBarProps) {
  const [query, setQuery] = useState(defaultValue)
  const navigate = useNavigate()

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault()
    const trimmed = query.trim()
    if (trimmed) {
      navigate(`/search?query=${encodeURIComponent(trimmed)}`)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="relative w-full max-w-md">
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder={placeholder}
        className="w-full rounded-full border border-input bg-background px-4 py-2.5 pr-10 text-sm outline-none transition-colors focus:border-primary focus:ring-1 focus:ring-ring"
      />
      <button
        type="submit"
        className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
      >
        <Search className="h-4 w-4" />
      </button>
    </form>
  )
}
