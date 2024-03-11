// Common Utils :: Business logic Helper
/**
 * Determines which business types are available for renewal, based on configuration
 */
public List<BusinessType> getRenewalBusinessTypes(List<BusinessType> businessTypes) {
    List<String> renewalIds;
    if (dataConfig.getBusinessTypes() != null) {
        renewalIds = dataConfig.getBusinessTypes().getRenewal();
    } else {
        renewalIds = null;
    }

    if (renewalIds == null || renewalIds.size() == 0) {
        return businessTypes.stream().filter(bt ->
                "RENEWAL".equals(bt.getId()) ||
                "EXPANDING".equals(bt.getId()))
                .collect(Collectors.toList());
        // 'non-price forbes' tenant has business_type_view without a numeric id column
    }

    Predicate<BusinessType> renewalPredicate = type -> renewalIds.stream().anyMatch(id -> type.getId().equals(id));
    return businessTypes.stream().filter(renewalPredicate).collect(Collectors.toList());

}

// Common Utils :: Business logic Helper Test
/**
 * Tests for {@link BusinessLogicHelper}
 *
 * @author James Smith
 * @since 11/04/2023
 */
class BusinessLogicHelperTest extends Specification {

    @Subject
    def underTest = new BusinessLogicHelper(null)

    def 'should get business types available for renewal, based on configured ids'() {
        given:
        List<BusinessType> businessTypes = businessTypesIds.stream().map(
                id -> new BusinessType(id: id)).collect(Collectors.toList())

        and:
        def config = new DataConfiguration(businessTypes: new BusinessTypes(renewal: renewalConfig))
        ReflectionTestUtils.setField(underTest, "dataConfig", config)

        when:
        def renewalBusinessTypes = underTest.getRenewalBusinessTypes(businessTypes)

        then:
        def renewalIds = renewalBusinessTypes.stream().map(BusinessType::getId).collect(Collectors.toList())
        renewalIds == expectedIds

        where: 'numeric ids mimic a "price forbes" tenant and non-numerics otherwise'
        renewalConfig      | businessTypesIds                                   | expectedIds
        null               | List.of('EXPANDING', 'NEW', 'PROSPECT', 'RENEWAL') | List.of('EXPANDING', 'RENEWAL')
        List.of()          | List.of('EXPANDING', 'NEW', 'PROSPECT', 'RENEWAL') | List.of('EXPANDING', 'RENEWAL')
        List.of('RENEWAL') | List.of('EXPANDING', 'NEW', 'PROSPECT', 'RENEWAL') | List.of('RENEWAL')
        List.of('2', '4')  | List.of('1', '2', '3', '4', '5')                   | List.of('2', '4')

    }

}