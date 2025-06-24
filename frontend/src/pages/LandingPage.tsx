import React, { useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { motion, useInView } from 'framer-motion';
import Header from '../components/Header';
import { 
  ShieldCheckIcon, 
  KeyIcon, 
  DevicePhoneMobileIcon,
  LockClosedIcon,
  EyeSlashIcon,
  CloudArrowUpIcon,
  CheckCircleIcon,
  StarIcon
} from '@heroicons/react/24/outline';

const LandingPage: React.FC = () => {
  const heroRef = useRef(null);
  const featuresRef = useRef(null);
  const securityRef = useRef(null);
  const ctaRef = useRef(null);

  const heroInView = useInView(heroRef, { once: true });
  const featuresInView = useInView(featuresRef, { once: true });
  const securityInView = useInView(securityRef, { once: true });
  const ctaInView = useInView(ctaRef, { once: true });

  const features = [
    {
      icon: KeyIcon,
      title: 'Advanced Password Generation',
      description: 'Generate ultra-secure passwords with customizable length and character sets, automatically checked against breach databases.'
    },
    {
      icon: LockClosedIcon,
      title: 'Military-Grade Encryption',
      description: 'Your data is protected with AES-256 encryption, the same standard used by governments and financial institutions.'
    },
    {
      icon: DevicePhoneMobileIcon,
      title: 'Cross-Platform Sync',
      description: 'Access your passwords securely across all your devices with real-time synchronization and offline capabilities.'
    },
    {
      icon: EyeSlashIcon,
      title: 'Zero-Knowledge Architecture',
      description: 'Your master password never leaves your device. We can\'t see your data, and neither can anyone else.'
    },
    {
      icon: ShieldCheckIcon,
      title: 'Two-Factor Authentication',
      description: 'Add an extra layer of security with TOTP-based 2FA, protecting your vault even if your password is compromised.'
    },
    {
      icon: CloudArrowUpIcon,
      title: 'Secure Backup & Recovery',
      description: 'Automatic encrypted backups ensure your data is safe, with easy recovery options when you need them.'
    }
  ];

  const testimonials = [
    {
      name: 'Sarah Johnson',
      role: 'Cybersecurity Professional',
      content: 'Finally, a password manager that I trust with my most sensitive data. The security features are enterprise-grade.',
      rating: 5
    },
    {
      name: 'Michael Chen',
      role: 'Software Developer',
      content: 'The user experience is incredible. It\'s powerful yet simple to use. The breach checking feature saved me multiple times.',
      rating: 5
    },
    {
      name: 'Emma Rodriguez',
      role: 'Business Owner',
      content: 'SecureVault has transformed how our team manages passwords. The security and convenience are unmatched.',
      rating: 5
    }
  ];

  return (
    <div className="min-h-screen bg-dark-950">
      <Header transparent />
      
      {/* Hero Section */}
      <section 
        ref={heroRef}
        className="relative min-h-screen flex items-center justify-center overflow-hidden"
        style={{
          backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), url('https://images.unsplash.com/photo-1593407089396-93f0c7a575f0')`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      >
        <div className="absolute inset-0 bg-gradient-to-br from-primary-900/20 to-accent-blue/20"></div>
        <div className="pattern-bg absolute inset-0 opacity-10"></div>
        
        <div className="container-max relative z-10 text-center">
          <motion.div
            initial={{ opacity: 0, y: 50 }}
            animate={heroInView ? { opacity: 1, y: 0 } : { opacity: 0, y: 50 }}
            transition={{ duration: 0.8 }}
            className="max-w-4xl mx-auto"
          >
            <h1 className="text-5xl md:text-7xl font-bold font-display mb-6 leading-tight">
              <span className="text-white">Your Digital Life,</span>
              <br />
              <span className="text-gradient">Perfectly Secured</span>
            </h1>
            
            <p className="text-xl md:text-2xl text-gray-300 mb-12 max-w-3xl mx-auto font-light">
              The most advanced password manager with military-grade encryption, 
              zero-knowledge architecture, and breach monitoring. Your security is our obsession.
            </p>
            
            <div className="flex flex-col sm:flex-row gap-6 justify-center items-center">
              <Link
                to="/signin"
                className="btn-primary text-lg px-8 py-4 animate-glow"
              >
                Start Free Trial
              </Link>
              <button className="btn-secondary text-lg px-8 py-4">
                Watch Demo
              </button>
            </div>
            
            <div className="mt-16 flex items-center justify-center space-x-8 text-sm text-gray-400">
              <div className="flex items-center">
                <CheckCircleIcon className="h-5 w-5 text-green-400 mr-2" />
                14-day free trial
              </div>
              <div className="flex items-center">
                <CheckCircleIcon className="h-5 w-5 text-green-400 mr-2" />
                No credit card required
              </div>
              <div className="flex items-center">
                <CheckCircleIcon className="h-5 w-5 text-green-400 mr-2" />
                Cancel anytime
              </div>
            </div>
          </motion.div>
        </div>
      </section>

      {/* Features Section */}
      <section ref={featuresRef} id="features" className="section-padding">
        <div className="container-max">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={featuresInView ? { opacity: 1, y: 0 } : { opacity: 0, y: 30 }}
            transition={{ duration: 0.6 }}
            className="text-center mb-20"
          >
            <h2 className="text-4xl md:text-5xl font-bold font-display mb-6">
              <span className="text-gradient">Enterprise-Grade Features</span>
            </h2>
            <p className="text-xl text-gray-300 max-w-3xl mx-auto">
              Built for individuals and teams who refuse to compromise on security
            </p>
          </motion.div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <motion.div
                key={feature.title}
                initial={{ opacity: 0, y: 30 }}
                animate={featuresInView ? { opacity: 1, y: 0 } : { opacity: 0, y: 30 }}
                transition={{ duration: 0.6, delay: index * 0.1 }}
                className="glass-effect-dark p-8 rounded-2xl card-hover group"
              >
                <div className="bg-gradient-to-br from-primary-500 to-accent-blue p-3 rounded-xl w-fit mb-6 group-hover:scale-110 transition-transform duration-300">
                  <feature.icon className="h-6 w-6 text-white" />
                </div>
                <h3 className="text-xl font-semibold mb-4 text-white">{feature.title}</h3>
                <p className="text-gray-300 leading-relaxed">{feature.description}</p>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* Security Section */}
      <section 
        ref={securityRef} 
        id="security" 
        className="section-padding"
        style={{
          backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0.8)), url('https://images.pexels.com/photos/8728290/pexels-photo-8728290.jpeg')`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundAttachment: 'fixed',
        }}
      >
        <div className="container-max">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={securityInView ? { opacity: 1, y: 0 } : { opacity: 0, y: 30 }}
            transition={{ duration: 0.6 }}
            className="text-center mb-20"
          >
            <h2 className="text-4xl md:text-5xl font-bold font-display mb-6 text-white">
              Uncompromising <span className="text-gradient">Security</span>
            </h2>
            <p className="text-xl text-gray-300 max-w-3xl mx-auto">
              Your trust is earned through transparency and proven security measures
            </p>
          </motion.div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            <motion.div
              initial={{ opacity: 0, x: -30 }}
              animate={securityInView ? { opacity: 1, x: 0 } : { opacity: 0, x: -30 }}
              transition={{ duration: 0.6 }}
              className="space-y-8"
            >
              <div className="glass-effect-dark p-6 rounded-xl">
                <h3 className="text-2xl font-semibold mb-4 text-white">AES-256 Encryption</h3>
                <p className="text-gray-300">The same encryption standard used by banks and governments worldwide protects your data.</p>
              </div>
              
              <div className="glass-effect-dark p-6 rounded-xl">
                <h3 className="text-2xl font-semibold mb-4 text-white">Zero-Knowledge Architecture</h3>
                <p className="text-gray-300">We never see your passwords. Your master key never leaves your device, ensuring complete privacy.</p>
              </div>
              
              <div className="glass-effect-dark p-6 rounded-xl">
                <h3 className="text-2xl font-semibold mb-4 text-white">Continuous Monitoring</h3>
                <p className="text-gray-300">Real-time breach monitoring alerts you immediately if any of your passwords are compromised.</p>
              </div>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, x: 30 }}
              animate={securityInView ? { opacity: 1, x: 0 } : { opacity: 0, x: 30 }}
              transition={{ duration: 0.6, delay: 0.2 }}
              className="glass-effect-dark p-8 rounded-2xl"
            >
              <h3 className="text-2xl font-semibold mb-6 text-white">Security Certifications</h3>
              <div className="space-y-4">
                <div className="flex items-center">
                  <CheckCircleIcon className="h-5 w-5 text-green-400 mr-3" />
                  <span className="text-gray-300">SOC 2 Type II Certified</span>
                </div>
                <div className="flex items-center">
                  <CheckCircleIcon className="h-5 w-5 text-green-400 mr-3" />
                  <span className="text-gray-300">ISO 27001 Compliant</span>
                </div>
                <div className="flex items-center">
                  <CheckCircleIcon className="h-5 w-5 text-green-400 mr-3" />
                  <span className="text-gray-300">GDPR Compliant</span>
                </div>
                <div className="flex items-center">
                  <CheckCircleIcon className="h-5 w-5 text-green-400 mr-3" />
                  <span className="text-gray-300">Regular Security Audits</span>
                </div>
              </div>
            </motion.div>
          </div>
        </div>
      </section>

      {/* Testimonials Section */}
      <section className="section-padding">
        <div className="container-max">
          <div className="text-center mb-20">
            <h2 className="text-4xl md:text-5xl font-bold font-display mb-6">
              <span className="text-gradient">Trusted by Professionals</span>
            </h2>
            <p className="text-xl text-gray-300 max-w-3xl mx-auto">
              Join thousands of security-conscious users who trust SecureVault
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {testimonials.map((testimonial, index) => (
              <motion.div
                key={testimonial.name}
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: index * 0.1 }}
                className="glass-effect-dark p-8 rounded-2xl card-hover"
              >
                <div className="flex mb-4">
                  {[...Array(testimonial.rating)].map((_, i) => (
                    <StarIcon key={i} className="h-5 w-5 text-yellow-400 fill-current" />
                  ))}
                </div>
                <p className="text-gray-300 mb-6 italic">"{testimonial.content}"</p>
                <div>
                  <h4 className="text-white font-semibold">{testimonial.name}</h4>
                  <p className="text-gray-400">{testimonial.role}</p>
                </div>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section 
        ref={ctaRef}
        className="section-padding"
        style={{
          backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0.8)), url('https://images.unsplash.com/photo-1529261233619-6afa28f5da3d')`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundAttachment: 'fixed',
        }}
      >
        <div className="container-max text-center">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={ctaInView ? { opacity: 1, y: 0 } : { opacity: 0, y: 30 }}
            transition={{ duration: 0.6 }}
            className="max-w-4xl mx-auto"
          >
            <h2 className="text-4xl md:text-6xl font-bold font-display mb-6 text-white">
              Ready to <span className="text-gradient">Secure Your Digital Life</span>?
            </h2>
            <p className="text-xl text-gray-300 mb-12 max-w-2xl mx-auto">
              Join millions of users who trust SecureVault to protect their most valuable digital assets. 
              Start your free trial today.
            </p>
            
            <div className="flex flex-col sm:flex-row gap-6 justify-center items-center mb-12">
              <Link
                to="/signin"
                className="btn-primary text-xl px-10 py-4 animate-glow"
              >
                Start Free Trial
              </Link>
              <Link
                to="/login"
                className="btn-secondary text-xl px-10 py-4"
              >
                Sign In
              </Link>
            </div>
            
            <div className="glass-effect-dark p-6 rounded-xl max-w-2xl mx-auto">
              <p className="text-gray-300 mb-4">
                <strong className="text-white">30-day money-back guarantee</strong> • No setup fees • Cancel anytime
              </p>
              <p className="text-sm text-gray-400">
                Your security is our priority. Try SecureVault risk-free.
              </p>
            </div>
          </motion.div>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-white/10 py-12">
        <div className="container-max">
          <div className="flex flex-col md:flex-row justify-between items-center">
            <div className="flex items-center space-x-2 mb-4 md:mb-0">
              <div className="bg-gradient-to-br from-primary-500 to-accent-blue p-2 rounded-xl">
                <ShieldCheckIcon className="h-6 w-6 text-white" />
              </div>
              <span className="text-xl font-bold font-display text-gradient">
                SecureVault
              </span>
            </div>
            
            <div className="flex space-x-8 text-gray-400">
              <a href="#" className="hover:text-white transition-colors">Privacy Policy</a>
              <a href="#" className="hover:text-white transition-colors">Terms of Service</a>
              <a href="#" className="hover:text-white transition-colors">Support</a>
            </div>
          </div>
          
          <div className="mt-8 pt-8 border-t border-white/10 text-center text-gray-400">
            <p>&copy; 2024 SecureVault. All rights reserved. Built with security in mind.</p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default LandingPage;