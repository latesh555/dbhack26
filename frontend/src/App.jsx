import { useState } from "react";
import {Routes, Route} from "react-router-dom";
import Header from "./components/Header.jsx";
import Screen1Upload from "./components/Screen1Upload.jsx";
import Screen2Dashboard from "./components/Screen2Dashboard.jsx";
import Screen3Engineering from "./components/Screen3Engineering.jsx";

export default function App() {
  const [screen, setScreen] = useState(1);

  const navigate = (s) => {
    setScreen(s);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  return (
    <div style={{ minHeight: "100vh", background: "var(--gray-50)" }}>
       {location.pathname !== "/" && <Header navigate={navigate} screen={screen} />}
      <main>
        <Routes>
          <Route path="/" element={<Screen1Upload />} />
          <Route path="/dashboard" element={<Screen2Dashboard />} />
          <Route path="/engineering" element={<Screen3Engineering />} />
        </Routes>
      </main>
    </div>
  );
}
