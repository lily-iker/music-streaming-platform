import { Loader } from 'lucide-react'

const Loading = () => {
  return (
    <div className='h-screen w-full flex items-center justify-center'>
        <Loader className='size-8 text-theme-500 animate-spin' />
    </div>
  )
}

export default Loading