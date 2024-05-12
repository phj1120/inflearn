import coursesData from './coursesData';
import type { CourseWithPath } from '~/types/course';

interface CoursesReturn {
  courses: CourseWithPath[];
}

export const useCourses = (): CoursesReturn => {
  const courses = coursesData.map((item) => ({
    ...item,
    rating: item.rating.toFixed(1), // 5.0
    reviewsCount: item.reviewsCount.toLocaleString(), // 1,234
    studentCount: item.studentCount.toLocaleString(), // 1,234
    path: `/course/${item.courseSlug}`,
  }));
  return { courses };
};
