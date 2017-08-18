package com.example.fqzhang.myapplication.Util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by fqzhang on 2017/8/18.
 */

public class FragmentExchangeController {

    public FragmentExchangeController() {
    }

    public static void replaceFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag) {
        replaceFragment(fragmentManager, targetFragment, tag, 16908290);
    }

    public static void replaceFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag, int postion) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(postion, targetFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void initFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag) {
        initFragment(fragmentManager, targetFragment, tag, 16908290);
    }

    public static void initFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag, int postion) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(postion, targetFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void initFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag, int postion, int animIn, int animOut, int animCloseIn, int animCloseOut) {
        if(!DeviceUtil.getAnimationSetting(FoundationContextHolder.context)) {
            animIn = 0;
            animOut = 0;
            animCloseIn = 0;
            animCloseOut = 0;
        } else {
            if(animIn < 0) {
                animIn = 0;
            }

            if(animOut < 0) {
                animOut = 0;
            }

            if(animCloseIn < 0) {
                animCloseIn = 0;
            }

            if(animCloseOut < 0) {
                animCloseOut = 0;
            }
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(animIn, animOut, animCloseIn, animCloseOut);
        transaction.add(postion, targetFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, String tag) {
        addFragment(supportFragmentManager, baseDialogFragment, 16908290, tag);
    }

    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        try {
            FragmentTransaction e = supportFragmentManager.beginTransaction();
//            e.setCustomAnimations(anim.common_anim_fragment_in, anim.common_anim_fragment_out, anim.common_anim_fragment_close_in, anim.common_anim_fragment_close_out);
            Fragment fragment = supportFragmentManager.findFragmentById(content);
            if(fragment != null) {
                if(fragment instanceof FragmentManager.OnBackStackChangedListener) {
                    supportFragmentManager.addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener)fragment);
                }

                e.hide(fragment);
            }

            e.add(content, baseDialogFragment, tag);
            e.addToBackStack(tag);
            e.commitAllowingStateLoss();
        } catch (IllegalStateException var6) {
            var6.printStackTrace();
        }

    }

    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag, int animIn, int animOut, int animCloseIn, int animCloseOut) {
        if(animIn < 0) {
            animIn = 0;
        }

        if(animOut < 0) {
            animOut = 0;
        }

        if(animCloseIn < 0) {
            animCloseIn = 0;
        }

        if(animCloseOut < 0) {
            animCloseOut = 0;
        }

        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.setCustomAnimations(animIn, animOut, animCloseIn, animCloseOut);
        Fragment fragment = supportFragmentManager.findFragmentById(content);
        if(fragment != null) {
            if(fragment instanceof FragmentManager.OnBackStackChangedListener) {
                supportFragmentManager.addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener)fragment);
            }

            transaction.hide(fragment);
        }

        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addWithoutAnimFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, String tag) {
        addWithoutAnimFragment(supportFragmentManager, baseDialogFragment, 16908290, tag);
    }

    public static void addWithoutAnimFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentWithOutBackStack(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentImmediately(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
    }

    public static void addWithoutStackFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(content, baseDialogFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void addWithAnimWithoutStackFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
//        transaction.setCustomAnimations(anim.common_anim_fragment_bottom_in, 0, 0, 0);
        Fragment fragment = supportFragmentManager.findFragmentById(content);
        if(fragment != null) {
            if(fragment instanceof FragmentManager.OnBackStackChangedListener) {
                supportFragmentManager.addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener)fragment);
            }

            transaction.hide(fragment);
        }

        transaction.add(content, baseDialogFragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void removeFragment(FragmentManager fragmentManager, String tag) {
        if(fragmentManager != null) {
            try {
                if(fragmentManager != null && fragmentManager.findFragmentByTag(tag) != null) {
                    fragmentManager.popBackStack(tag, 1);
                }
            } catch (Exception var4) {
                ;
            }

            Fragment targetFragment = fragmentManager.findFragmentByTag(tag);
            if(targetFragment != null) {
                FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
                localFragmentTransaction.remove(targetFragment);
                localFragmentTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
        }

    }

    public static void removeFragment(FragmentManager fragmentManager, Fragment targetFragment) {
        if(fragmentManager != null) {
            String tag = targetFragment.getTag();

            try {
                try {
                    if(fragmentManager != null && fragmentManager.findFragmentByTag(tag) != null) {
                        fragmentManager.popBackStack(tag, 1);
                    }
                } catch (Exception var5) {
                    ;
                }

                FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
                localFragmentTransaction.remove(targetFragment);
                localFragmentTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
                Fragment fragment = fragmentManager.findFragmentByTag(tag);
                if(fragment != null) {
                    localFragmentTransaction.remove(fragment);
                    localFragmentTransaction.commitAllowingStateLoss();
                    fragmentManager.executePendingTransactions();
                }
            } catch (Exception var6) {
                ;
            }
        }

    }

    public static ArrayList<Fragment> getAllFragments(FragmentActivity fragmentActivity) {
        ArrayList fragments = null;
        if(fragmentActivity == null) {
            return fragments;
        } else {
            FragmentManager fragmentMgr = fragmentActivity.getSupportFragmentManager();
            if(fragmentMgr == null) {
                return fragments;
            } else {
                try {
                    Class e = fragmentMgr.getClass();
                    Field field = e.getDeclaredField("mAdded");
                    field.setAccessible(true);
                    fragments = new ArrayList();
                    if(field.get(fragmentMgr) != null) {
                        fragments.addAll((ArrayList)field.get(fragmentMgr));
                    }
                } catch (Exception var5) {
//                    if(Env.isTestEnv()) {
//                        LogUtil.e("Exception", var5);
//                    }
                }

                return fragments;
            }
        }
    }
}
