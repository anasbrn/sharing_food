//package com.example.sharing_food.Activity.Dashboard
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.CircularProgressIndicator
//import androidx.compose.runtime.*
//import androidx.compose.runtime.snapshots.SnapshotStateList
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import com.example.sharing_food.Domain.BannerModel
//import com.example.sharing_food.R
//import com.google.accompanist.pager.*
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//@Composable
//fun Banner(
//    banners: SnapshotStateList<BannerModel>,
//    showBannerLoading: Boolean
//) {
//    if (showBannerLoading) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    } else {
//        Banners(banners = banners)
//    }
//}
//
//@OptIn(ExperimentalPagerApi::class)
//@Composable
//fun Banners(banners: List<BannerModel>) {
//    AutoSlidingCarousel(banners = banners)
//}
//
//@OptIn(ExperimentalPagerApi::class)
//@Composable
//fun AutoSlidingCarousel(
//    modifier: Modifier = Modifier,
//    pagerState: PagerState = rememberPagerState(),
//    banners: List<BannerModel>
//) {
//    val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(key1 = pagerState.currentPage) {
//        delay(3000)
//        val nextPage = (pagerState.currentPage + 1) % banners.size
//        coroutineScope.launch {
//            pagerState.animateScrollToPage(nextPage)
//        }
//    }
//
//    Column(modifier = modifier.fillMaxWidth()) {
//        HorizontalPager(
//            count = banners.size,
//            state = pagerState,
//            modifier = Modifier.fillMaxWidth()
//        ) { page ->
//            // 🔵 LIGNE 79
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(banners[page].image) // ← .image doit exister
//                    .crossfade(true)
//                    .build(),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .padding(16.dp)
//                    .clip(RoundedCornerShape(10.dp))
//                    .height(150.dp)
//                    .fillMaxWidth()
//            )
//        }
//
//        // 🔵 LIGNE 98
//        DotIndicator(
//            modifier = Modifier
//                .padding(top = 8.dp)
//                .align(Alignment.CenterHorizontally),
//            totalDots = banners.size,
//            selectedIndex = pagerState.currentPage,
//            selectedColor = colorResource(id = R.color.grey),
//            unSelectedColor = Color.LightGray
//        )
//    }
//}
//
//// --------------------- DotIndicator ---------------------
//
//@Composable
//fun DotIndicator(
//    modifier: Modifier = Modifier,
//    totalDots: Int,
//    selectedIndex: Int,
//    selectedColor: Color = colorResource(id = R.color.grey),
//    unSelectedColor: Color = Color.LightGray,
//    dotSize: Dp = 8.dp,
//    dotSpacing: Dp = 8.dp
//) {
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        repeat(totalDots) { index ->
//            val color = if (index == selectedIndex) selectedColor else unSelectedColor
//            Box(
//                modifier = Modifier
//                    .size(dotSize)
//                    .clip(CircleShape)
//                    .background(color)
//                    .then(
//                        if (index != totalDots - 1) Modifier.padding(end = dotSpacing) else Modifier
//                    )
//            )
//        }
//    }
//    // 🔵 LIGNE 109 — fin du DotIndicator
//}
