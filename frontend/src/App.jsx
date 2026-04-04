import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Link } from 'react-router-dom';
import API from './api/axios';
import AdminDashboard from './pages/AdminDashboard';
import StaffDashboard from './pages/StaffDashboard';
import GuestDashboard from './pages/GuestDashboard'; 
import Register from './pages/Register';
import { Toaster } from 'react-hot-toast'; // 👈 Import ready

// --- Login Component ---
const Login = ({ setSession }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const { data } = await API.post('/auth/login', { username, password });
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data));
      setSession(data); 
    } catch (err) {
      alert("Invalid credentials. Access denied.");
    }
  };

  return (
    <div className="min-h-screen bg-luxury-black flex items-center justify-center">
      <div className="bg-white/5 p-10 rounded-2xl border border-gold/30 backdrop-blur-md w-96 text-center shadow-2xl">
        <h1 className="text-gold text-3xl mb-8 tracking-widest uppercase font-serif">7-Star Hotel</h1>
        <form onSubmit={handleLogin} className="space-y-6">
          <input 
            type="text" placeholder="ID Number" 
            className="w-full p-3 bg-transparent border border-gray-700 text-white rounded focus:border-gold outline-none transition-all"
            onChange={(e) => setUsername(e.target.value)}
          />
          <input 
            type="password" placeholder="Passcode" 
            className="w-full p-3 bg-transparent border border-gray-700 text-white rounded focus:border-gold outline-none transition-all"
            onChange={(e) => setPassword(e.target.value)}
          />
          <button className="w-full bg-gold text-black font-bold py-3 rounded hover:bg-yellow-500 hover:scale-105 transition-all cursor-pointer">
            AUTHENTICATE
          </button>
          
          <p className="mt-8 text-gray-500 text-sm">
            New to the establishment? <Link to="/register" className="text-gold hover:underline">Register for Access</Link>
          </p>
        </form>
      </div>
    </div>
  );
};

// --- Main App ---
function App() {
  const [session, setSession] = useState(() => {
    const savedUser = localStorage.getItem('user');
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const getRedirectPath = () => {
    if (!session) return "/";
    if (session.role === 'admin') return "/admin";
    if (session.role === 'guest') return "/guest"; 
    return "/staff"; 
  };

  return (
    <Router>
      {/* ✅ ADDED THE TOASTER HERE */}
      <Toaster position="top-center" reverseOrder={false} />
      
      <Routes>
        <Route path="/" element={!session ? <Login setSession={setSession} /> : <Navigate to={getRedirectPath()} />} />
        <Route path="/admin" element={session?.role === 'admin' ? <AdminDashboard /> : <Navigate to="/" />} />
        <Route path="/register" element={<Register />} />
        <Route path="/guest" element={session?.role === 'guest' ? <GuestDashboard /> : <Navigate to="/" />} />
        <Route path="/staff" element={session && ['cleaner', 'chef', 'manager'].includes(session.role) ? <StaffDashboard /> : <Navigate to="/" />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </Router>
  );
}

export default App;