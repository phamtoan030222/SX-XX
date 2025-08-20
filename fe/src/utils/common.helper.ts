export default function formatDate(timestamp: number | string): string {
  if (!timestamp) return ''

  // Ép sang number
  let timeNum = Number(timestamp)

  // Nếu nhỏ hơn 1e12 thì là giây, cần nhân 1000 để ra milliseconds
  if (timeNum < 1e12) {
    timeNum *= 1000
  }

  const date = new Date(timeNum)
  if (Number.isNaN(date.getTime())) return ''

  const day = String(date.getDate()).padStart(2, '0')
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const year = date.getFullYear()

  return `${day}/${month}/${year}`
}
