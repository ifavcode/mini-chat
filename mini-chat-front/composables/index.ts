export const useUser = () => {
  return useState('user', () => ({
    userId: 1,
    eventSourceListener: null,
    curGroup: {}
  }))
}