WheelPicker
===
iOS-style scroll wheel picker

[![codebeat badge](https://codebeat.co/badges/346d671e-d683-4471-be7d-a3d6f742de86)](https://codebeat.co/projects/github-com-ty0x2333-wheelpicker-master)

Read this in other languages: [*简体中文*](README.zh-cn.md)

Preview
---
![Preview](resources/preview.gif)

|Day|Night|
|---|---|
|![Day](resources/day.png)|![Night](resources/night.png)|

Custom
---
Customize the style of the item in the same way as the `RecyclerView`.
You need to provide a `BaseWheelPickerView.Adapter` and a `BaseWheelPickerView.ViewHolder`.

***Special note: the itemView of the ViewHolder needs to be a fixed height view.***

> After all, `WheelPicker` is based on `RecyclerView`. Doing so will ensure as much performance and reliability as possible.

Custom WheelPicker please refer to [CustomWheelPickerView.kt](app/src/main/java/sh/tyy/wheelpicker/example/custom/CustomWheelPickerView.kt)

This is what the custom example looks like:
![Custom](resources/custom.png)

**For more complex customizations (e.g. multiple columns), please refer to [WeekdayTimePickerView](WheelPicker/src/main/java/sh/tyy/wheelpicker/WeekdayTimePickerView.kt)**

Inspiration and Reference
---
WheelPicker is inspired by [devilist/RecyclerWheelPicker](https://github.com/devilist/RecyclerWheelPicker).

License
---
WheelPicker is available under the Apache 2.0 license. See the LICENSE file for more info.