import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { useAuth } from '../contexts/AuthContext';
import { useToast } from '../contexts/ToastContext';
import { 
  ShieldCheckIcon,
  PlusIcon,
  EyeIcon,
  EyeSlashIcon,
  ClipboardIcon,
  TrashIcon,
  MagnifyingGlassIcon,
  ExclamationTriangleIcon,
  ArrowRightOnRectangleIcon,
  Cog6ToothIcon,
  KeyIcon,
  CheckCircleIcon,
  XCircleIcon,
  GlobeAltIcon,
  EnvelopeIcon,
  LockClosedIcon
} from '@heroicons/react/24/outline';

interface Credential {
  id: string;
  title: string;
  username: string;
  email: string;
  password: string;
  website: string;
  notes: string;
  category: string;
  createdAt: string;
  updatedAt: string;
  isCompromised?: boolean;
}

const Dashboard: React.FC = () => {
  const { user, logout } = useAuth();
  const { success, error } = useToast();
  
  const [activeTab, setActiveTab] = useState('credentials');
  const [credentials, setCredentials] = useState<Credential[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [showAddModal, setShowAddModal] = useState(false);
  const [showPasswordGenerator, setShowPasswordGenerator] = useState(false);
  const [selectedCredential, setSelectedCredential] = useState<Credential | null>(null);
  const [visiblePasswords, setVisiblePasswords] = useState<Set<string>>(new Set());

  // Mock credentials data
  useEffect(() => {
    const mockCredentials: Credential[] = [
      {
        id: '1',
        title: 'Gmail',
        username: 'john.doe',
        email: 'john.doe@gmail.com',
        password: 'SecurePass123!',
        website: 'https://gmail.com',
        notes: 'Personal email account',
        category: 'Email',
        createdAt: '2024-01-15',
        updatedAt: '2024-01-15',
        isCompromised: false
      },
      {
        id: '2',
        title: 'GitHub',
        username: 'johndoe',
        email: 'john.doe@gmail.com',
        password: 'GitHubSecure456!',
        website: 'https://github.com',
        notes: 'Development account',
        category: 'Development',
        createdAt: '2024-01-10',
        updatedAt: '2024-01-10',
        isCompromised: false
      },
      {
        id: '3',
        title: 'Banking',
        username: 'john.doe',
        email: 'john.doe@gmail.com',
        password: 'BankSecure789!',
        website: 'https://mybank.com',
        notes: 'Main banking account',
        category: 'Finance',
        createdAt: '2024-01-05',
        updatedAt: '2024-01-05',
        isCompromised: true
      }
    ];
    setCredentials(mockCredentials);
  }, []);

  const filteredCredentials = credentials.filter(credential =>
    credential.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
    credential.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
    credential.website.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const togglePasswordVisibility = (id: string) => {
    const newVisible = new Set(visiblePasswords);
    if (newVisible.has(id)) {
      newVisible.delete(id);
    } else {
      newVisible.add(id);
    }
    setVisiblePasswords(newVisible);
  };

  const copyToClipboard = async (text: string, type: string) => {
    try {
      await navigator.clipboard.writeText(text);
      success(`${type} copied to clipboard`);
    } catch (err) {
      error('Failed to copy to clipboard');
    }
  };

  const deleteCredential = (id: string) => {
    setCredentials(prev => prev.filter(cred => cred.id !== id));
    success('Credential deleted successfully');
  };

  const checkBreaches = async () => {
    // Mock breach checking
    success('Checking passwords against breach databases...');
    
    setTimeout(() => {
      const compromisedCount = credentials.filter(c => c.isCompromised).length;
      if (compromisedCount > 0) {
        error(`Found ${compromisedCount} compromised password(s)!`);
      } else {
        success('All passwords are secure!');
      }
    }, 2000);
  };

  const handleLogout = () => {
    logout();
    success('Logged out successfully');
  };

  return (
    <div className="min-h-screen bg-dark-950">
      {/* Header */}
      <header className="glass-effect-dark border-b border-white/10 sticky top-0 z-40">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center space-x-4">
              <div className="bg-gradient-to-br from-primary-500 to-accent-blue p-2 rounded-xl">
                <ShieldCheckIcon className="h-6 w-6 text-white" />
              </div>
              <div>
                <h1 className="text-xl font-bold text-gradient">SecureVault</h1>
                <p className="text-sm text-gray-400">Welcome back, {user?.email}</p>
              </div>
            </div>
            
            <div className="flex items-center space-x-4">
              <button
                onClick={checkBreaches}
                className="btn-secondary flex items-center space-x-2"
              >
                <ExclamationTriangleIcon className="h-4 w-4" />
                <span>Check Breaches</span>
              </button>
              
              <button
                onClick={handleLogout}
                className="text-gray-400 hover:text-white transition-colors flex items-center space-x-2"
              >
                <ArrowRightOnRectangleIcon className="h-5 w-5" />
                <span>Logout</span>
              </button>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          {/* Sidebar */}
          <div className="lg:col-span-1">
            <nav className="glass-effect-dark p-6 rounded-2xl">
              <h2 className="text-lg font-semibold text-white mb-6">Menu</h2>
              <ul className="space-y-2">
                <li>
                  <button
                    onClick={() => setActiveTab('credentials')}
                    className={`w-full text-left px-4 py-3 rounded-xl transition-all duration-200 flex items-center space-x-3 ${
                      activeTab === 'credentials' 
                        ? 'bg-primary-600 text-white' 
                        : 'text-gray-300 hover:bg-white/10 hover:text-white'
                    }`}
                  >
                    <LockClosedIcon className="h-5 w-5" />
                    <span>All Credentials</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => setShowAddModal(true)}
                    className="w-full text-left px-4 py-3 rounded-xl transition-all duration-200 flex items-center space-x-3 text-gray-300 hover:bg-white/10 hover:text-white"
                  >
                    <PlusIcon className="h-5 w-5" />
                    <span>Add Credential</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => setShowPasswordGenerator(true)}
                    className="w-full text-left px-4 py-3 rounded-xl transition-all duration-200 flex items-center space-x-3 text-gray-300 hover:bg-white/10 hover:text-white"
                  >
                    <KeyIcon className="h-5 w-5" />
                    <span>Password Generator</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => setActiveTab('settings')}
                    className={`w-full text-left px-4 py-3 rounded-xl transition-all duration-200 flex items-center space-x-3 ${
                      activeTab === 'settings' 
                        ? 'bg-primary-600 text-white' 
                        : 'text-gray-300 hover:bg-white/10 hover:text-white'
                    }`}
                  >
                    <Cog6ToothIcon className="h-5 w-5" />
                    <span>Settings</span>
                  </button>
                </li>
              </ul>
            </nav>

            {/* Security Status */}
            <div className="glass-effect-dark p-6 rounded-2xl mt-6">
              <h3 className="text-lg font-semibold text-white mb-4">Security Status</h3>
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <span className="text-gray-300">Total Passwords</span>
                  <span className="text-white font-semibold">{credentials.length}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-300">Compromised</span>
                  <span className="text-red-400 font-semibold">
                    {credentials.filter(c => c.isCompromised).length}
                  </span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-300">Secure</span>
                  <span className="text-green-400 font-semibold">
                    {credentials.filter(c => !c.isCompromised).length}
                  </span>
                </div>
              </div>
            </div>
          </div>

          {/* Main Content */}
          <div className="lg:col-span-3">
            {activeTab === 'credentials' && (
              <div className="space-y-6">
                {/* Search Bar */}
                <div className="glass-effect-dark p-6 rounded-2xl">
                  <div className="relative">
                    <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
                    <input
                      type="text"
                      placeholder="Search credentials..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                      className="input-field w-full pl-10"
                    />
                  </div>
                </div>

                {/* Credentials List */}
                <div className="space-y-4">
                  {filteredCredentials.map((credential) => (
                    <motion.div
                      key={credential.id}
                      initial={{ opacity: 0, y: 20 }}
                      animate={{ opacity: 1, y: 0 }}
                      className="glass-effect-dark p-6 rounded-2xl card-hover"
                    >
                      <div className="flex items-start justify-between">
                        <div className="flex-1">
                          <div className="flex items-center space-x-3 mb-3">
                            <div className="bg-gradient-to-br from-primary-500 to-accent-blue p-2 rounded-lg">
                              <GlobeAltIcon className="h-5 w-5 text-white" />
                            </div>
                            <div>
                              <h3 className="text-lg font-semibold text-white flex items-center space-x-2">
                                <span>{credential.title}</span>
                                {credential.isCompromised && (
                                  <ExclamationTriangleIcon className="h-5 w-5 text-red-400" />
                                )}
                              </h3>
                              <p className="text-gray-400 text-sm">{credential.website}</p>
                            </div>
                          </div>
                          
                          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                              <label className="text-xs text-gray-400 uppercase tracking-wide">Username</label>
                              <div className="flex items-center space-x-2 mt-1">
                                <span className="text-white">{credential.username}</span>
                                <button
                                  onClick={() => copyToClipboard(credential.username, 'Username')}
                                  className="text-gray-400 hover:text-white transition-colors"
                                >
                                  <ClipboardIcon className="h-4 w-4" />
                                </button>
                              </div>
                            </div>
                            
                            <div>
                              <label className="text-xs text-gray-400 uppercase tracking-wide">Email</label>
                              <div className="flex items-center space-x-2 mt-1">
                                <span className="text-white">{credential.email}</span>
                                <button
                                  onClick={() => copyToClipboard(credential.email, 'Email')}
                                  className="text-gray-400 hover:text-white transition-colors"
                                >
                                  <ClipboardIcon className="h-4 w-4" />
                                </button>
                              </div>
                            </div>
                            
                            <div className="md:col-span-2">
                              <label className="text-xs text-gray-400 uppercase tracking-wide">Password</label>
                              <div className="flex items-center space-x-2 mt-1">
                                <span className="text-white font-mono">
                                  {visiblePasswords.has(credential.id) 
                                    ? credential.password 
                                    : '••••••••••••'
                                  }
                                </span>
                                <button
                                  onClick={() => togglePasswordVisibility(credential.id)}
                                  className="text-gray-400 hover:text-white transition-colors"
                                >
                                  {visiblePasswords.has(credential.id) ? (
                                    <EyeSlashIcon className="h-4 w-4" />
                                  ) : (
                                    <EyeIcon className="h-4 w-4" />
                                  )}
                                </button>
                                <button
                                  onClick={() => copyToClipboard(credential.password, 'Password')}
                                  className="text-gray-400 hover:text-white transition-colors"
                                >
                                  <ClipboardIcon className="h-4 w-4" />
                                </button>
                              </div>
                            </div>
                          </div>
                          
                          {credential.notes && (
                            <div className="mt-4">
                              <label className="text-xs text-gray-400 uppercase tracking-wide">Notes</label>
                              <p className="text-gray-300 mt-1">{credential.notes}</p>
                            </div>
                          )}
                        </div>
                        
                        <div className="flex items-center space-x-2 ml-4">
                          <button
                            onClick={() => deleteCredential(credential.id)}
                            className="text-red-400 hover:text-red-300 transition-colors p-2 rounded-lg hover:bg-red-900/20"
                          >
                            <TrashIcon className="h-5 w-5" />
                          </button>
                        </div>
                      </div>
                    </motion.div>
                  ))}
                  
                  {filteredCredentials.length === 0 && (
                    <div className="glass-effect-dark p-12 rounded-2xl text-center">
                      <LockClosedIcon className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                      <h3 className="text-xl font-semibold text-white mb-2">No credentials found</h3>
                      <p className="text-gray-400 mb-6">
                        {searchTerm ? 'Try adjusting your search terms' : 'Add your first credential to get started'}
                      </p>
                      <button
                        onClick={() => setShowAddModal(true)}
                        className="btn-primary"
                      >
                        Add Credential
                      </button>
                    </div>
                  )}
                </div>
              </div>
            )}

            {activeTab === 'settings' && (
              <div className="glass-effect-dark p-8 rounded-2xl">
                <h2 className="text-2xl font-bold text-white mb-6">Account Settings</h2>
                <div className="space-y-6">
                  <div>
                    <h3 className="text-lg font-semibold text-white mb-3">Account Information</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div>
                        <label className="text-sm text-gray-400">Email</label>
                        <p className="text-white">{user?.email}</p>
                      </div>
                      <div>
                        <label className="text-sm text-gray-400">Two-Factor Auth</label>
                        <p className="text-white flex items-center space-x-2">
                          {user?.has2FA ? (
                            <>
                              <CheckCircleIcon className="h-5 w-5 text-green-400" />
                              <span>Enabled</span>
                            </>
                          ) : (
                            <>
                              <XCircleIcon className="h-5 w-5 text-red-400" />
                              <span>Disabled</span>
                            </>
                          )}
                        </p>
                      </div>
                    </div>
                  </div>
                  
                  <div className="pt-6 border-t border-white/10">
                    <h3 className="text-lg font-semibold text-white mb-3">Security Actions</h3>
                    <div className="space-y-3">
                      <button className="btn-secondary">Change Master Password</button>
                      <button className="btn-secondary">Setup 2FA</button>
                      <button className="btn-secondary">Export Data</button>
                      <button className="bg-red-600 hover:bg-red-700 text-white font-semibold py-3 px-6 rounded-xl transition-all duration-300">
                        Delete Account
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Add Credential Modal */}
      {showAddModal && (
        <AddCredentialModal 
          onClose={() => setShowAddModal(false)}
          onSave={(credential) => {
            setCredentials(prev => [...prev, { ...credential, id: Date.now().toString() }]);
            setShowAddModal(false);
            success('Credential added successfully');
          }}
        />
      )}

      {/* Password Generator Modal */}
      {showPasswordGenerator && (
        <PasswordGeneratorModal 
          onClose={() => setShowPasswordGenerator(false)}
        />
      )}
    </div>
  );
};

// Add Credential Modal Component
const AddCredentialModal: React.FC<{
  onClose: () => void;
  onSave: (credential: Omit<Credential, 'id'>) => void;
}> = ({ onClose, onSave }) => {
  const [formData, setFormData] = useState({
    title: '',
    username: '',
    email: '',
    password: '',
    website: '',
    notes: '',
    category: 'General',
    createdAt: new Date().toISOString().split('T')[0],
    updatedAt: new Date().toISOString().split('T')[0],
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave(formData);
  };

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <motion.div
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        className="glass-effect-dark p-8 rounded-2xl max-w-md w-full max-h-[90vh] overflow-y-auto"
      >
        <h2 className="text-2xl font-bold text-white mb-6">Add New Credential</h2>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-2">Title</label>
            <input
              type="text"
              value={formData.title}
              onChange={(e) => setFormData({...formData, title: e.target.value})}
              className="input-field w-full"
              placeholder="e.g., Gmail, GitHub"
              required
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-2">Website</label>
            <input
              type="url"
              value={formData.website}
              onChange={(e) => setFormData({...formData, website: e.target.value})}
              className="input-field w-full"
              placeholder="https://example.com"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-2">Username</label>
            <input
              type="text"
              value={formData.username}
              onChange={(e) => setFormData({...formData, username: e.target.value})}
              className="input-field w-full"
              placeholder="Username"
              required
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-2">Email</label>
            <input
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({...formData, email: e.target.value})}
              className="input-field w-full"
              placeholder="email@example.com"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-2">Password</label>
            <input
              type="password"
              value={formData.password}
              onChange={(e) => setFormData({...formData, password: e.target.value})}
              className="input-field w-full"
              placeholder="Password"
              required
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-2">Notes</label>
            <textarea
              value={formData.notes}
              onChange={(e) => setFormData({...formData, notes: e.target.value})}
              className="input-field w-full"
              rows={3}
              placeholder="Additional notes..."
            />
          </div>
          
          <div className="flex space-x-4 pt-4">
            <button type="submit" className="btn-primary flex-1">
              Save Credential
            </button>
            <button type="button" onClick={onClose} className="btn-secondary flex-1">
              Cancel
            </button>
          </div>
        </form>
      </motion.div>
    </div>
  );
};

// Password Generator Modal Component
const PasswordGeneratorModal: React.FC<{
  onClose: () => void;
}> = ({ onClose }) => {
  const [password, setPassword] = useState('');
  const [options, setOptions] = useState({
    length: 16,
    uppercase: true,
    lowercase: true,
    numbers: true,
    symbols: true,
  });

  const { success } = useToast();

  const generatePassword = () => {
    const uppercase = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    const lowercase = 'abcdefghijklmnopqrstuvwxyz';
    const numbers = '0123456789';
    const symbols = '!@#$%^&*()_+-=[]{}|;:,.<>?';
    
    let charset = '';
    if (options.uppercase) charset += uppercase;
    if (options.lowercase) charset += lowercase;
    if (options.numbers) charset += numbers;
    if (options.symbols) charset += symbols;
    
    let result = '';
    for (let i = 0; i < options.length; i++) {
      result += charset.charAt(Math.floor(Math.random() * charset.length));
    }
    
    setPassword(result);
  };

  const copyPassword = async () => {
    if (password) {
      try {
        await navigator.clipboard.writeText(password);
        success('Password copied to clipboard');
      } catch (err) {
        console.error('Failed to copy password');
      }
    }
  };

  React.useEffect(() => {
    generatePassword();
  }, [options]);

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <motion.div
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        className="glass-effect-dark p-8 rounded-2xl max-w-md w-full"
      >
        <h2 className="text-2xl font-bold text-white mb-6">Password Generator</h2>
        
        <div className="space-y-6">
          {/* Generated Password */}
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-2">Generated Password</label>
            <div className="flex items-center space-x-2">
              <input
                type="text"
                value={password}
                readOnly
                className="input-field flex-1 font-mono"
              />
              <button
                onClick={copyPassword}
                className="btn-secondary"
              >
                <ClipboardIcon className="h-5 w-5" />
              </button>
            </div>
          </div>
          
          {/* Length Slider */}
          <div>
            <label className="block text-sm font-medium text-gray-300 mb-2">
              Length: {options.length}
            </label>
            <input
              type="range"
              min="8"
              max="64"
              value={options.length}
              onChange={(e) => setOptions({...options, length: parseInt(e.target.value)})}
              className="w-full"
            />
          </div>
          
          {/* Character Options */}
          <div className="space-y-3">
            <label className="flex items-center space-x-3">
              <input
                type="checkbox"
                checked={options.uppercase}
                onChange={(e) => setOptions({...options, uppercase: e.target.checked})}
                className="rounded border-gray-600 bg-transparent text-primary-500"
              />
              <span className="text-gray-300">Uppercase Letters (A-Z)</span>
            </label>
            
            <label className="flex items-center space-x-3">
              <input
                type="checkbox"
                checked={options.lowercase}
                onChange={(e) => setOptions({...options, lowercase: e.target.checked})}
                className="rounded border-gray-600 bg-transparent text-primary-500"
              />
              <span className="text-gray-300">Lowercase Letters (a-z)</span>
            </label>
            
            <label className="flex items-center space-x-3">
              <input
                type="checkbox"
                checked={options.numbers}
                onChange={(e) => setOptions({...options, numbers: e.target.checked})}
                className="rounded border-gray-600 bg-transparent text-primary-500"
              />
              <span className="text-gray-300">Numbers (0-9)</span>
            </label>
            
            <label className="flex items-center space-x-3">
              <input
                type="checkbox"
                checked={options.symbols}
                onChange={(e) => setOptions({...options, symbols: e.target.checked})}
                className="rounded border-gray-600 bg-transparent text-primary-500"
              />
              <span className="text-gray-300">Symbols (!@#$%^&*)</span>
            </label>
          </div>
          
          <div className="flex space-x-4 pt-4">
            <button onClick={generatePassword} className="btn-primary flex-1">
              Generate New
            </button>
            <button onClick={onClose} className="btn-secondary flex-1">
              Close
            </button>
          </div>
        </div>
      </motion.div>
    </div>
  );
};

export default Dashboard;