import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import API from '../api/axios';
import { UserPlus } from 'lucide-react';
import toast from 'react-hot-toast'; // 👈 Added for luxury notifications

const Register = () => {
  const [formData, setFormData] = useState({ name: '', username: '', password: '', role: 'guest' });
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await API.post('/auth/register', formData);
      
      // ✅ 7-Star Notification
      toast.success("Identity Verified. Your 7-star journey begins now!", {
        style: { background: '#1a1a1a', color: '#d4af37', border: '1px solid #d4af37' },
        iconTheme: { primary: '#d4af37', secondary: '#000' }
      });

      // Brief delay so they can see the beautiful success message
      setTimeout(() => navigate('/'), 2000); 
    } catch (err) {
      // ✅ Handle error with toast
      const errorMsg = err.response?.data?.message || "Registration denied. System ID may be taken.";
      toast.error(errorMsg);
    }
  };

  return (
    <div className="min-h-screen bg-[#050505] flex items-center justify-center p-4">
      <div className="bg-white/5 p-10 rounded-3xl border border-gold/20 backdrop-blur-xl w-full max-w-md shadow-2xl text-center">
        <div className="flex justify-center mb-6">
          <div className="p-4 bg-gold/10 rounded-full border border-gold/30">
            <UserPlus className="text-gold" size={32} />
          </div>
        </div>
        <h1 className="text-gold text-3xl mb-2 tracking-tighter uppercase font-serif">Join the Elite</h1>
        <p className="text-gray-500 text-xs mb-8 uppercase tracking-widest">Create your 7-Star Identity</p>
        
        <form onSubmit={handleRegister} className="space-y-5">
          <input 
            type="text" placeholder="Full Name" required
            className="w-full p-4 bg-black/50 border border-gray-800 text-white rounded-xl focus:border-gold outline-none transition-all placeholder:text-gray-600"
            onChange={(e) => setFormData({...formData, name: e.target.value})}
          />
          <input 
            type="text" placeholder="System ID (Username)" required
            className="w-full p-4 bg-black/50 border border-gray-800 text-white rounded-xl focus:border-gold outline-none transition-all placeholder:text-gray-600"
            onChange={(e) => setFormData({...formData, username: e.target.value})}
          />
          <input 
            type="password" placeholder="Passcode" required
            className="w-full p-4 bg-black/50 border border-gray-800 text-white rounded-xl focus:border-gold outline-none transition-all placeholder:text-gray-600"
            onChange={(e) => setFormData({...formData, password: e.target.value})}
          />
          
          <button className="w-full bg-white text-black font-bold py-4 rounded-xl hover:bg-gold transition-all cursor-pointer uppercase tracking-widest text-xs active:scale-95 shadow-lg">
            Initialize Account
          </button>
        </form>

        <p className="mt-8 text-gray-500 text-sm">
          Already a member? <Link to="/" className="text-gold hover:underline transition-all">Authenticate here</Link>
        </p>
      </div>
    </div>
  );
};

export default Register;