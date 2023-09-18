package com.example.onboarding.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.onboarding.R
import com.example.onboarding.adapter.ViewPagerAdapter
import com.example.onboarding.databinding.ActivityMainBinding
import com.example.onboarding.model.OnBoardingData
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityMainBinding
    // view pager
    private lateinit var onBoardingViewPager: ViewPager
    // adapter
    private lateinit var onBoardingViewPagerAdapter: ViewPagerAdapter
    // tab layout
    private lateinit var tabLayout: TabLayout
    private var position = 0
    // shared preferences
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // share preferences
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)

        if(restorePrefData()) {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setPrefData()

            // on boarding appearance
            val onBoardingData: MutableList<OnBoardingData> = ArrayList()
            onBoardingData.add(OnBoardingData(getString(R.string.onboarding_title), getString(R.string.onboarding_desc),
                R.drawable.onboarding_1
            ))
            onBoardingData.add(OnBoardingData(getString(R.string.onboarding_title), getString(R.string.onboarding_desc),
                R.drawable.onboarding_2
            ))
            onBoardingData.add(OnBoardingData(getString(R.string.onboarding_title), getString(R.string.onboarding_desc),
                R.drawable.onboarding_3
            ))
            onBoardingData.add(OnBoardingData(getString(R.string.onboarding_title), getString(R.string.onboarding_desc),
                R.drawable.onboarding_4
            ))

            onBoardingViewPager = binding.viewPager
            tabLayout = binding.tabLayout

            setOnboardingViewpagerAdapter(onBoardingData)

            // to the next position
            binding.next.setOnClickListener {
                if (position < onBoardingData.size) {
                    position++
                    onBoardingViewPager.currentItem = position
                }
                if(position == onBoardingData.size) {
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                }
            }

            // to the previous position
            binding.back.setOnClickListener {
                if (position > 0) {
                    position--
                    onBoardingViewPager.currentItem = position
                }
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    position = tab?.position ?: 0
                    if (tab?.position == onBoardingData.size - 1) {
                        binding.next.text = getString(R.string.next)
                    } else {
                        binding.next.text = getString(R.string.next)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Do nothing
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Do nothing
                }
            })
        }
    }

    private fun setOnboardingViewpagerAdapter(onBoardingData: List<OnBoardingData>) {
        onBoardingViewPagerAdapter = ViewPagerAdapter(this, onBoardingData)
        onBoardingViewPager.adapter = onBoardingViewPagerAdapter
        tabLayout.setupWithViewPager(onBoardingViewPager)
    }

    private fun setPrefData() {
        sharedPreferences.edit()
            .putBoolean("isFirstTimeRun", true)
            .apply()
    }

    private fun restorePrefData(): Boolean {
        return sharedPreferences.getBoolean("isFirstTimeRun", false)
    }
}