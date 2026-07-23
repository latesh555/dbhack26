import { useState } from 'react'
import Header from './components/Header.jsx'
import Screen1Upload from './components/Screen1Upload.jsx'
import Screen2Dashboard from './components/Screen2Dashboard.jsx'
import Screen3Engineering from './components/Screen3Engineering.jsx'

export default function App() {
  const [screen, setScreen] = useState(1)

  const navigate = (s) => {
    setScreen(s)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  return (
    <div style={{ minHeight: '100vh', background: 'var(--gray-50)' }}>
      {screen !== 1 && <Header navigate={navigate} screen={screen} />}
      <main>
        {screen === 1 && <Screen1Upload navigate={navigate} />}
        {screen === 2 && <Screen2Dashboard navigate={navigate} />}
        {screen === 3 && <Screen3Engineering navigate={navigate} />}
      </main>
    </div>
  )
}
