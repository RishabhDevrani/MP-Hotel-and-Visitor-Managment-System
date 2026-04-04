import React, { useEffect, useState } from 'react';
import API from '../api/axios';
import { Bed, Users, Calendar, LogOut, X, Plus, ShieldCheck } from 'lucide-react';
import toast from 'react-hot-toast'; // 👈 Added for 7-star notifications

const AdminDashboard = () => {
  // --- 1. STATES ---
  const [rooms, setRooms] = useState([]);
  const [staffList, setStaffList] = useState([]);
  const [loading, setLoading] = useState(true);
  
  const [showHireModal, setShowHireModal] = useState(false);
  const [showRoomModal, setShowRoomModal] = useState(false);
  const [showTaskModal, setShowTaskModal] = useState(false);
  
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [staffData, setStaffData] = useState({ name: '', username: '', password: '', role: 'cleaner', department: '' });
  const [roomData, setRoomData] = useState({ roomNumber: '', type: 'Standard', basePrice: '' });
  const [taskData, setTaskData] = useState({ assignedTo: '', taskType: 'Cleaning', urgency: 'Normal' });

  // --- 2. DATA FETCHING ---
  useEffect(() => { 
    fetchRooms(); 
    fetchStaff();
  }, []);

  const fetchRooms = async () => {
    try {
      const { data } = await API.get('/admin/rooms'); 
      setRooms(data);
      setLoading(false);
    } catch (err) { console.error("Room fetch failed"); setLoading(false); }
  };

  const fetchStaff = async () => {
    try {
      // NOTE: Ensure your backend /admin/staff route uses the $nin: ['admin', 'guest'] filter
      const { data } = await API.get('/admin/staff');
      setStaffList(data);
    } catch (err) { console.error("Staff fetch failed"); }
  };

  // --- 3. HANDLERS ---
  const handleHireSubmit = async (e) => {
    e.preventDefault();
    try {
      await API.post('/admin/employee', staffData);
      toast.success(`Recruitment Complete: ${staffData.name} is now active.`);
      setShowHireModal(false);
      fetchStaff();
    } catch (err) { toast.error(err.response?.data?.message || "Hiring failed"); }
  };

  const handleRoomSubmit = async (e) => {
    e.preventDefault();
    try {
      await API.post('/admin/room', roomData);
      toast.success(`Construction Complete: Room ${roomData.roomNumber} created.`);
      setShowRoomModal(false);
      fetchRooms();
    } catch (err) { toast.error(err.response?.data?.message || "Build failed"); }
  };

  const handleTaskSubmit = async (e) => {
    e.preventDefault();
    try {
      await API.post('/tasks', { ...taskData, roomId: selectedRoom._id });
      toast.success(`Order Confirmed: Staff deployed to Room ${selectedRoom.roomNumber}`);
      setShowTaskModal(false);
      fetchRooms(); 
    } catch (err) { toast.error("Deployment failed"); }
  };

  const handleLogout = () => {
    localStorage.clear();
    toast.success("Grandmaster Logged Out");
    setTimeout(() => window.location.href = '/', 1000);
  };

  // ✅ HELPER: Filter out guests from the UI logic
  const filteredStaff = staffList.filter(s => s.role !== 'guest');

  return (
    <div className="min-h-screen bg-[#050505] text-white p-6 md:p-10 font-sans selection:bg-gold selection:text-black">
      
      {/* HEADER */}
      <div className="flex justify-between items-center border-b border-gold/30 pb-6 mb-10">
        <div className="flex items-center gap-3">
          <ShieldCheck className="text-gold" size={32} />
          <h1 className="text-gold text-3xl tracking-tighter uppercase font-serif">Grandmaster Control</h1>
        </div>
        <button onClick={handleLogout} className="text-gray-400 hover:text-gold flex items-center gap-2 transition-all cursor-pointer">
          <LogOut size={20}/> <span className="hidden sm:inline uppercase text-[10px] tracking-widest font-bold">Clock Out</span>
        </button>
      </div>

      {/* DASHBOARD GRID */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        {/* ROOM STATUS CARD */}
        <div className="bg-white/5 p-6 rounded-3xl border border-gold/10 shadow-2xl backdrop-blur-sm">
          <div className="flex justify-between items-center mb-6 text-gold">
            <h2 className="text-lg font-bold flex items-center gap-2 font-serif uppercase tracking-widest italic"><Bed size={22} /> Room Status</h2>
            <button onClick={() => setShowRoomModal(true)} className="p-1.5 hover:bg-gold hover:text-black rounded-full border border-gold/40 transition-all cursor-pointer">
              <Plus size={18}/>
            </button>
          </div>
          <div className="space-y-3 max-h-[500px] overflow-y-auto pr-2 custom-scrollbar">
            {rooms.map(room => (
              <div 
                key={room._id} 
                onClick={() => { setSelectedRoom(room); setShowTaskModal(true); }}
                className="flex justify-between items-center bg-black/60 p-4 rounded-2xl border border-white/5 hover:border-gold/50 cursor-pointer transition-all group"
              >
                <div>
                  <span className="font-mono text-2xl block group-hover:text-gold transition-colors">#{room.roomNumber}</span>
                  <span className="text-[10px] text-gray-500 uppercase tracking-widest italic">Assign Staff</span>
                </div>
                <span className={`text-[10px] px-3 py-1 rounded-full font-bold uppercase tracking-tighter border ${
                  room.currentStatus === 'Available' ? 'bg-green-900/20 text-green-400 border-green-500/20' : 'bg-red-900/20 text-red-400 border-red-500/20'
                }`}>
                  {room.currentStatus}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* STAFF REGISTRY CARD */}
        <div className="bg-white/5 p-6 rounded-3xl border border-gold/10 flex flex-col justify-between shadow-2xl">
          <div>
            <h2 className="text-gold text-lg font-bold mb-4 flex items-center gap-2 font-serif uppercase tracking-widest italic"><Users size={22} /> Staff Registry</h2>
            <p className="text-gray-400 text-sm mb-6 leading-relaxed">Only professional staff members are listed below. Guests are excluded from work tasks.</p>
            <div className="space-y-2">
              <div className="flex justify-between text-xs border-b border-white/5 pb-2 text-gray-300 uppercase tracking-widest">
                <span>Active Cleaners</span> 
                <span className="text-gold font-mono">{filteredStaff.filter(s => s.role === 'cleaner').length}</span>
              </div>
              <div className="flex justify-between text-xs border-b border-white/5 pb-2 text-gray-300 uppercase tracking-widest">
                <span>Culinary Experts</span> 
                <span className="text-gold font-mono">{filteredStaff.filter(s => s.role === 'chef').length}</span>
              </div>
            </div>
          </div>
          <button onClick={() => setShowHireModal(true)} className="w-full py-4 mt-8 bg-gold/5 border border-gold text-gold rounded-xl hover:bg-gold hover:text-black transition-all font-bold uppercase tracking-widest text-xs cursor-pointer shadow-lg active:scale-95">
            Recruit New Staff
          </button>
        </div>

        {/* ANALYTICS / LOGISTICS */}
        <div className="bg-white/5 p-6 rounded-3xl border border-gold/10 shadow-2xl">
          <h2 className="text-gold text-lg font-bold mb-4 flex items-center gap-2 font-serif uppercase tracking-widest italic"><Calendar size={22} /> Logistics</h2>
          <div className="space-y-4">
            <button onClick={() => setShowRoomModal(true)} className="w-full py-4 bg-white text-black rounded-xl hover:bg-gold transition-all font-bold uppercase tracking-widest text-xs shadow-lg active:scale-95 cursor-pointer">
              Architect New Suite
            </button>
            <div className="p-6 bg-black/40 rounded-2xl border border-white/5 text-center shadow-inner">
              <p className="text-gray-500 text-[10px] uppercase tracking-widest mb-1">Total Capacity</p>
              <p className="text-4xl font-serif text-white">{rooms.length}</p>
              <p className="text-[10px] text-gold/60 mt-2 italic font-serif">LUXURY SUITES CONSTRUCTED</p>
            </div>
          </div>
        </div>
      </div>

      {/* --- MODAL: TASK DEPLOYMENT --- */}
      {showTaskModal && (
        <div className="fixed inset-0 bg-black/95 backdrop-blur-md flex items-center justify-center p-4 z-50">
          <div className="bg-zinc-900 border border-gold/40 p-8 rounded-3xl w-full max-w-md relative animate-in zoom-in duration-300">
            <button onClick={() => setShowTaskModal(false)} className="absolute top-6 right-6 text-gray-400 hover:text-white cursor-pointer"><X size={24} /></button>
            <h2 className="text-gold text-2xl font-serif mb-6 text-center uppercase tracking-widest italic">Assign Staff: #{selectedRoom?.roomNumber}</h2>
            <form onSubmit={handleTaskSubmit} className="space-y-5">
              <div className="space-y-1">
                <label className="text-[10px] text-gray-500 uppercase ml-1">Elite Personnel Only</label>
                <select required className="w-full p-4 bg-black border border-gray-800 rounded-xl text-white outline-none focus:border-gold cursor-pointer"
                  onChange={(e) => setTaskData({...taskData, assignedTo: e.target.value})}>
                  <option value="">Select Professional Staff...</option>
                  {/* ✅ Filtered list only shows non-guests */}
                  {filteredStaff.map(s => <option key={s._id} value={s._id}>{s.name} ({s.role.toUpperCase()})</option>)}
                </select>
              </div>
              <div className="space-y-1">
                <label className="text-[10px] text-gray-500 uppercase ml-1">Service Objective</label>
                <select className="w-full p-4 bg-black border border-gray-800 rounded-xl text-white outline-none focus:border-gold cursor-pointer"
                  onChange={(e) => setTaskData({...taskData, taskType: e.target.value})}>
                  <option value="Cleaning">Sanitize & Clean</option>
                  <option value="Maintenance">Technical Repair</option>
                  <option value="Food Service">Gourmet Delivery</option>
                </select>
              </div>
              <button className="w-full bg-gold text-black font-bold py-4 rounded-xl hover:bg-yellow-500 transition-all uppercase text-xs tracking-widest mt-4 cursor-pointer active:scale-95 shadow-xl">
                Deploy Now
              </button>
            </form>
          </div>
        </div>
      )}

      {/* --- MODAL: ROOM CONSTRUCTION --- */}
      {showRoomModal && (
        <div className="fixed inset-0 bg-black/90 backdrop-blur-md flex items-center justify-center p-4 z-50">
          <div className="bg-zinc-900 border border-gold/40 p-8 rounded-3xl w-full max-w-md relative shadow-2xl animate-in fade-in slide-in-from-bottom-10 duration-500">
            <button onClick={() => setShowRoomModal(false)} className="absolute top-6 right-6 text-gray-500 hover:text-white transition-colors cursor-pointer"><X size={24} /></button>
            <h2 className="text-gold text-2xl font-serif mb-8 text-center uppercase tracking-widest italic">New Construction</h2>
            <form onSubmit={handleRoomSubmit} className="space-y-5">
              <input type="number" placeholder="Room Number" required className="w-full p-4 bg-black border border-gray-800 rounded-xl text-white focus:border-gold outline-none" 
                onChange={(e) => setRoomData({...roomData, roomNumber: e.target.value})} />
              <select className="bg-black border border-gray-800 p-4 rounded-xl w-full text-white outline-none focus:border-gold cursor-pointer"
                onChange={(e) => setRoomData({...roomData, type: e.target.value})}>
                <option value="Standard">Standard Suite</option>
                <option value="Deluxe">Deluxe Parlor</option>
                <option value="Penthouse">Royal Penthouse</option>
              </select>
              <input type="number" placeholder="Nightly Rate ($)" required className="w-full p-4 bg-black border border-gray-800 rounded-xl text-white focus:border-gold outline-none" 
                onChange={(e) => setRoomData({...roomData, basePrice: e.target.value})} />
              <button className="w-full bg-white text-black font-bold py-4 rounded-xl hover:bg-gold transition-all uppercase text-xs tracking-widest cursor-pointer active:scale-95 shadow-lg">Authorize Build</button>
            </form>
          </div>
        </div>
      )}

      {/* --- MODAL: STAFF RECRUITMENT --- */}
      {showHireModal && (
        <div className="fixed inset-0 bg-black/90 backdrop-blur-md flex items-center justify-center p-4 z-50">
          <div className="bg-zinc-900 border border-gold/40 p-8 rounded-3xl w-full max-w-md relative shadow-2xl animate-in fade-in slide-in-from-bottom-10 duration-500">
            <button onClick={() => setShowHireModal(false)} className="absolute top-6 right-6 text-gray-500 hover:text-white transition-colors cursor-pointer"><X size={24} /></button>
            <h2 className="text-gold text-2xl font-serif mb-8 text-center uppercase tracking-widest italic">Recruitment</h2>
            <form onSubmit={handleHireSubmit} className="space-y-5">
              <input type="text" placeholder="Full Name" required className="w-full p-4 bg-black border border-gray-800 rounded-xl text-white focus:border-gold outline-none" 
                onChange={(e) => setStaffData({...staffData, name: e.target.value})} />
              <input type="text" placeholder="Access Username" required className="w-full p-4 bg-black border border-gray-800 rounded-xl text-white focus:border-gold outline-none" 
                onChange={(e) => setStaffData({...staffData, username: e.target.value})} />
              <input type="password" placeholder="Passcode" required className="w-full p-4 bg-black border border-gray-800 rounded-xl text-white focus:border-gold outline-none" 
                onChange={(e) => setStaffData({...staffData, password: e.target.value})} />
              <select className="bg-black border border-gray-800 p-4 rounded-xl w-full text-white outline-none focus:border-gold cursor-pointer"
                onChange={(e) => setStaffData({...staffData, role: e.target.value})}>
                <option value="cleaner">Housekeeping (Cleaner)</option>
                <option value="chef">Executive Chef</option>
                <option value="manager">Floor Manager</option>
              </select>
              <button className="w-full bg-white text-black font-bold py-4 rounded-xl hover:bg-gold transition-all uppercase text-xs tracking-widest cursor-pointer active:scale-95 shadow-lg">Confirm Access</button>
            </form>
          </div>
        </div>
      )}

    </div>
  );
};

export default AdminDashboard;