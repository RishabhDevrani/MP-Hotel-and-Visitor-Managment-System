import React, { useEffect, useState } from 'react';
import API from '../api/axios';
import { Hotel, Star, CheckCircle, LogOut, UtensilsCrossed, Coffee, Pizza } from 'lucide-react';
import toast from 'react-hot-toast';

const GuestDashboard = () => {
  const [availableRooms, setAvailableRooms] = useState([]);
  const [myBooking, setMyBooking] = useState(null);
  const user = JSON.parse(localStorage.getItem('user'));

  // 1. Gourmet Menu Data
  const menuItems = [
    { id: 1, name: "Truffle Risotto", price: 45, icon: <UtensilsCrossed size={16}/> },
    { id: 2, name: "Beluga Caviar", price: 120, icon: <Star size={16}/> },
    { id: 3, name: "Blueberry Croissant", price: 18, icon: <Coffee size={16}/> },
    { id: 4, name: "Artisanal Pizza", price: 35, icon: <Pizza size={16}/> }
  ];

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const available = await API.get('/bookings/available');
      setAvailableRooms(available.data);
      // Optional: Logic to fetch myBooking from backend would go here
    } catch (err) { 
      console.error(err); 
    }
  };

  // 2. Room Service Handler
  const handleOrderFood = async (item) => {
    try {
      // Sending order to the backend
      await API.post('/orders', {
        items: [{ name: item.name, price: item.price }],
        totalAmount: item.price,
        roomNumber: myBooking?.room?.roomNumber || "Suite Pending"
      });

      toast.success(`Gourmet Request Received: ${item.name} is being prepared.`, {
        icon: '👨‍🍳',
        style: { background: '#1a1a1a', color: '#d4af37', border: '1px solid #d4af37' }
      });
    } catch (err) {
      toast.error("The Kitchen is currently at capacity.");
    }
  };

  const handleBook = async (roomId) => {
    try {
      const { data } = await API.post('/bookings/book', { 
        roomId, 
        guestName: user.name 
      });
      toast.success('Suite Reserved! Welcome to the Grand Sanctuary.', {
        style: { background: '#1a1a1a', color: '#d4af37', border: '1px solid #d4af37' }
      });
      fetchData();
    } catch (err) { 
      toast.error(err.response?.data?.message || "Booking failed."); 
    }
  };

  const handleCheckout = async (id) => {
    try {
      await API.put(`/bookings/${id}/checkout`);
      toast.success("Thank you for your stay! We hope to see you again.");
      setMyBooking(null);
      fetchData();
    } catch (err) { 
      toast.error("Checkout failed."); 
    }
  };

  return (
    <div className="min-h-screen bg-[#050505] text-white p-6 md:p-12 font-sans selection:bg-gold selection:text-black">
      {/* Header */}
      <div className="flex justify-between items-center mb-12 border-b border-white/5 pb-8">
        <div>
          <h1 className="text-3xl font-serif text-gold tracking-widest uppercase">The Grand Sanctuary</h1>
          <p className="text-gray-500 text-sm mt-1">Refined Hospitality for <span className="text-white italic">{user?.name}</span></p>
        </div>
        <button 
          onClick={() => {localStorage.clear(); window.location.href='/';}} 
          className="text-gray-500 hover:text-gold transition-colors cursor-pointer"
        >
          <LogOut size={20}/>
        </button>
      </div>

      {/* Main Content */}
      <div className="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-12">
        
        {/* Booking Options & Room Service */}
        <div className="lg:col-span-2 space-y-16">
          
          {/* Available Suites */}
          <section>
            <h2 className="text-xl mb-6 flex items-center gap-3 font-serif uppercase tracking-widest italic text-gold/80">
              <Hotel className="text-gold" size={20}/> Available Suites
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {availableRooms.map(room => (
                <div key={room._id} className="bg-zinc-900/40 border border-white/5 p-6 rounded-3xl hover:border-gold/30 transition-all group">
                  <div className="flex justify-between mb-4">
                    <span className="text-2xl font-mono">#{room.roomNumber}</span>
                    <div className="flex text-gold/60">
                      <Star size={12}/><Star size={12}/><Star size={12}/><Star size={12}/><Star size={12}/>
                    </div>
                  </div>
                  <p className="text-xs text-gray-400 uppercase tracking-widest mb-6">{room.type}</p>
                  <div className="flex justify-between items-center">
                    <p className="text-xl font-mono">${room.basePrice}<span className="text-[10px] text-gray-500">/NIGHT</span></p>
                    <button 
                      onClick={() => handleBook(room._id)} 
                      className="bg-white text-black px-6 py-2 rounded-full font-bold text-xs hover:bg-gold transition-all cursor-pointer transform active:scale-95 shadow-lg"
                    >
                      BOOK NOW
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </section>

          {/* 3. Room Service Menu Section */}
          <section className="bg-white/[0.02] border border-white/5 p-8 rounded-[2rem]">
            <h2 className="text-xl mb-8 flex items-center gap-3 font-serif uppercase tracking-widest italic text-gold/80">
              <UtensilsCrossed className="text-gold" size={20}/> In-Suite Dining
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {menuItems.map(item => (
                <div key={item.id} className="bg-black/40 border border-white/5 p-4 rounded-2xl flex justify-between items-center group hover:border-gold/20 transition-all">
                  <div className="flex items-center gap-4">
                    <div className="p-3 bg-white/5 rounded-xl text-gold group-hover:scale-110 transition-transform">
                      {item.icon}
                    </div>
                    <div>
                      <p className="text-sm font-bold tracking-wide">{item.name}</p>
                      <p className="text-gold font-mono text-xs">${item.price}</p>
                    </div>
                  </div>
                  <button 
                    onClick={() => handleOrderFood(item)}
                    className="px-4 py-2 bg-transparent border border-gold/30 text-gold rounded-lg text-[10px] font-bold hover:bg-gold hover:text-black transition-all cursor-pointer uppercase tracking-widest"
                  >
                    Order
                  </button>
                </div>
              ))}
            </div>
          </section>

        </div>

        {/* Guest Status / "Keycard" */}
        <div className="bg-zinc-900/80 border border-gold/20 p-8 rounded-3xl h-fit sticky top-12 shadow-[0_0_40px_rgba(212,175,55,0.05)] text-center">
          <h3 className="text-gold font-serif text-xl mb-8 uppercase tracking-widest">Digital Keycard</h3>
          {myBooking ? (
            <div className="animate-in fade-in duration-700">
              <div className="w-20 h-20 bg-gold/10 border border-gold/40 rounded-full flex items-center justify-center mx-auto mb-6">
                <CheckCircle className="text-gold" size={32}/>
              </div>
              <p className="text-sm text-gray-400 mb-1 uppercase tracking-tighter">Currently In</p>
              <p className="text-3xl font-mono mb-8">ROOM {myBooking.room?.roomNumber}</p>
              <button 
                onClick={() => handleCheckout(myBooking._id)} 
                className="w-full py-4 border border-red-500/50 text-red-500 rounded-xl hover:bg-red-500 hover:text-white transition-all font-bold uppercase text-[10px] tracking-widest cursor-pointer"
              >
                End Residence
              </button>
            </div>
          ) : (
            <div className="py-10 opacity-30 italic">
              <p className="text-sm text-gray-500">No active reservation found.</p>
              <div className="mt-4 flex justify-center"><Hotel size={24}/></div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default GuestDashboard;